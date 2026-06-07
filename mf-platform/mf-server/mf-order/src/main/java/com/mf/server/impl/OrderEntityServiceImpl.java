package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.client.ProductClient;
import com.mf.server.entity.OrderEntity;
import com.mf.server.entity.OrderItem;
import com.mf.server.mapper.OrderEntityMapper;
import com.mf.server.service.OrderEntityService;
import com.mf.server.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service @RequiredArgsConstructor
public class OrderEntityServiceImpl extends ServiceImpl<OrderEntityMapper, OrderEntity>
        implements OrderEntityService {
    private final OrderItemService itemService;
    private final ProductClient productClient;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> createOrder(Long userId, List<Map<String, Object>> items) {
        BigDecimal total = BigDecimal.ZERO;
        var orderItems = new ArrayList<OrderItem>();
        for (var item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            int quantity = Integer.parseInt(item.get("quantity").toString());
            Map<String, Object> resp = productClient.getProduct(productId);
            Map<String, Object> product = (Map<String, Object>) resp.get("data");
            int stock = Integer.parseInt(product.get("stock").toString());
            if (stock < quantity) throw new RuntimeException("商品「"+product.get("name")+"」库存不足");
            BigDecimal price = new BigDecimal(product.get("price").toString());
            var oi = new OrderItem();
            oi.setProductId(productId); oi.setProductName((String)product.get("name"));
            oi.setProductImage((String)product.get("coverImage")); oi.setPrice(price);
            oi.setQuantity(quantity); oi.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
            orderItems.add(oi); total = total.add(oi.getTotalPrice());
        }
        String orderNo = "MF"+UUID.randomUUID().toString().replace("-","").substring(0,16).toUpperCase();
        var order = new OrderEntity();
        order.setOrderNo(orderNo); order.setUserId(userId); order.setStatus("pending_pay");
        order.setTotalAmount(total); order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO); order.setPayAmount(total);
        order.setAddressSnapshot("{}"); save(order);
        for (var oi : orderItems) { oi.setOrderId(order.getId()); oi.setOrderNo(orderNo); itemService.save(oi); }
        kafkaTemplate.send("order-created", Map.of("userId",userId,"orderNo",orderNo,"items",items));
        return Map.of("orderId",order.getId(),"orderNo",orderNo);
    }
}
