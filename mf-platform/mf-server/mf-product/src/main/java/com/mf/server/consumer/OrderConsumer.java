package com.mf.server.consumer;

import com.mf.server.entity.Product;
import com.mf.server.entity.ShoppingCartItem;
import com.mf.server.service.ProductService;
import com.mf.server.service.ShoppingCartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final ProductService productService;
    private final ShoppingCartItemService cartService;
    private final JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    @KafkaListener(topics = "order-created", groupId = "mf-product-group")
    public void onOrderCreated(Map<String, Object> message) {
        Long userId = Long.valueOf(message.get("userId").toString());
        List<Map<String, Object>> items = (List<Map<String, Object>>) message.get("items");

        log.info("收到订单创建消息: userId={}, items={}", userId, items.size());

        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            int quantity = Integer.parseInt(item.get("quantity").toString());

            // 扣库存
            Product product = productService.getById(productId);
            if (product != null && product.getStock() >= quantity) {
                product.setStock(product.getStock() - quantity);
                product.setSalesCount(product.getSalesCount() + quantity);
                productService.updateById(product);
                log.info("库存已扣: productId={}, 剩余库存={}", productId, product.getStock());
            }

            // 清购物车（逻辑删除）
            jdbcTemplate.update(
                    "UPDATE shopping_cart_item SET deleted=1 WHERE user_id=? AND product_id=? AND deleted=0",
                    userId, productId);
        }
    }
}
