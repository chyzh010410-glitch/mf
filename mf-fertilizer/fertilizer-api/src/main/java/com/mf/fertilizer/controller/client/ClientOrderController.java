package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.client.OrderCreateDTO;
import com.mf.fertilizer.entity.*;
import com.mf.fertilizer.service.*;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import com.mf.fertilizer.vo.client.OrderVO;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/client/orders")
@RequiredArgsConstructor
public class ClientOrderController {

    private final OrderEntityService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final UserAddressService addressService;
    private final ShoppingCartItemService cartService;
    private final JdbcTemplate jdbcTemplate;
    private final StringRedisTemplate redisTemplate;
    private final NotificationService notificationService;

    private final ReentrantLock stockLock = new ReentrantLock();

    @PostMapping
    @Transactional
    public ResultVO<?> create(@Valid @RequestBody OrderCreateDTO dto) {
        Long userId = UserContext.getUserId();
        var address = addressService.getById(dto.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) return ResultVO.fail(400, "收货地址无效");

        String orderNo = "MF" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        var order = new OrderEntity();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        String addrJson = "{\"receiverName\":\"" + address.getReceiverName() + "\",\"receiverPhone\":\"" + address.getReceiverPhone() + "\",\"province\":\"" + address.getProvince() + "\",\"city\":\"" + address.getCity() + "\",\"district\":\"" + address.getDistrict() + "\",\"detail\":\"" + address.getDetail() + "\"}";
        order.setAddressSnapshot(addrJson);
        order.setStatus("pending_pay");
        order.setUserRemark(dto.getRemark());

        BigDecimal total = BigDecimal.ZERO;
        var items = new ArrayList<OrderItem>();

        // ReentrantLock 加锁，防止并发下单导致超卖
        stockLock.lock();
        try {
            for (var itemDto : dto.getItems()) {
                var product = productService.getById(itemDto.getProductId());
                if (product == null || product.getStatus() == 0) return ResultVO.fail(400, "商品「" + itemDto.getProductId() + "」不存在或已下架");
                if (product.getStock() < itemDto.getQuantity()) return ResultVO.fail(400, "商品「" + product.getName() + "」库存不足");

                String stockKey = "stock:product:" + product.getId();
                redisTemplate.opsForValue().setIfAbsent(stockKey, String.valueOf(product.getStock()));
                Long remaining = redisTemplate.opsForValue().decrement(stockKey, itemDto.getQuantity());
                if (remaining != null && remaining < 0) {
                    redisTemplate.opsForValue().increment(stockKey, itemDto.getQuantity());
                    return ResultVO.fail(400, "商品「" + product.getName() + "」库存不足");
                }

                var oi = new OrderItem();
                oi.setOrderNo(orderNo);
                oi.setProductId(product.getId());
                oi.setProductName(product.getName());
                oi.setProductImage(product.getCoverImage());
                oi.setPrice(product.getPrice());
                oi.setQuantity(itemDto.getQuantity());
                oi.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
                items.add(oi);
                total = total.add(oi.getTotalPrice());
                product.setStock(product.getStock() - itemDto.getQuantity());
                product.setSalesCount(product.getSalesCount() + itemDto.getQuantity());
                productService.updateById(product);
            }
        } finally {
            stockLock.unlock();
        }

        order.setTotalAmount(total);
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(total);
        orderService.save(order);
        for (var item : items) {
            item.setOrderId(order.getId());
            orderItemService.save(item);
        }
//        for (var itemDto : dto.getItems()) {
//            jdbcTemplate.update("DELETE FROM shopping_cart_item WHERE user_id = ? AND product_id = ?",
//                    userId, itemDto.getProductId());
//        }

        for (var itemDto : dto.getItems()) {
            cartService.lambdaUpdate()
                    .eq(ShoppingCartItem::getUserId, userId)  // 条件1：匹配user_id
                    .eq(ShoppingCartItem::getProductId, itemDto.getProductId())  // 条件2：匹配product_id
                    .remove();  // 触发逻辑删除，自动转为UPDATE
        }
        var result = new OrderCreateResult();
        result.setOrderId(order.getId());
        result.setOrderNo(orderNo);
        result.setPayAmount(total);

        // @Async 异步发送订单通知（不阻塞主线程返回）
        notificationService.sendOrderCreatedNotification(userId, orderNo);

        return ResultVO.success(result);
    }

    @GetMapping
    public ResultVO<PageVO<OrderVO>> list(@ModelAttribute PageDTO page,
                                          @RequestParam(required = false) String status) {
        Long userId = UserContext.getUserId();
        var wrapper = new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getUserId, userId)
                .eq(status != null, OrderEntity::getStatus, status)
                .orderByDesc(OrderEntity::getCreateTime);
        var p = orderService.page(new Page<>(page.getPage(), page.getSize()), wrapper);
        var records = new ArrayList<OrderVO>();
        for (var order : p.getRecords()) {
            records.add(toOrderVO(order));
        }
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), records));
    }

    @GetMapping("/{id}")
    public ResultVO<OrderVO> detail(@PathVariable Long id) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        return ResultVO.success(toOrderVO(order));
    }

    @PostMapping("/{id}/cancel")
    public ResultVO<?> cancel(@PathVariable Long id, @RequestBody(required = false) CancelRequest body) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        if (!"pending_pay".equals(order.getStatus())) return ResultVO.fail(400, "仅待付款订单可取消");
        order.setStatus("cancelled");
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason(body != null ? body.getReason() : null);
        orderService.updateById(order);
        return ResultVO.success();
    }

    @PostMapping("/{id}/confirm")
    public ResultVO<?> confirm(@PathVariable Long id) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        order.setStatus("completed");
        order.setCompleteTime(LocalDateTime.now());
        orderService.updateById(order);
        return ResultVO.success();
    }

    private OrderVO toOrderVO(OrderEntity order) {
        var vo = new OrderVO();
        vo.setId(order.getId()); vo.setOrderNo(order.getOrderNo());
        vo.setTotalAmount(order.getTotalAmount()); vo.setFreightAmount(order.getFreightAmount());
        vo.setDiscountAmount(order.getDiscountAmount()); vo.setPayAmount(order.getPayAmount());
        vo.setStatus(order.getStatus()); vo.setPaymentMethod(order.getPaymentMethod());
        vo.setPayTime(order.getPayTime()); vo.setShipTime(order.getShipTime());
        vo.setCompleteTime(order.getCompleteTime()); vo.setCancelTime(order.getCancelTime());
        vo.setAddressSnapshot(order.getAddressSnapshot());
        var items = orderItemService.lambdaQuery().eq(OrderItem::getOrderId, order.getId()).list();
        var itemVOs = new ArrayList<OrderVO.OrderItemVO>();
        for (var item : items) {
            var iv = new OrderVO.OrderItemVO();
            iv.setProductId(item.getProductId()); iv.setProductName(item.getProductName());
            iv.setProductImage(item.getProductImage()); iv.setPrice(item.getPrice());
            iv.setQuantity(item.getQuantity()); iv.setTotalPrice(item.getTotalPrice());
            itemVOs.add(iv);
        }
        vo.setItems(itemVOs);
        return vo;
    }

    @Data
    public static class OrderCreateResult {
        private Long orderId;
        private String orderNo;
        private BigDecimal payAmount;
    }

    @Data
    public static class CancelRequest {
        private String reason;
    }
}
