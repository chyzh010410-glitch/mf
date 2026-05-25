package com.mf.fertilizer.task;

import com.mf.fertilizer.entity.OrderEntity;
import com.mf.fertilizer.service.OrderEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutTask {

    private final OrderEntityService orderService;
    private final StringRedisTemplate redisTemplate;

    private static final String LOCK_KEY = "task:order_timeout:lock";
    private static final long TIMEOUT_MINUTES = 30;

    @Scheduled(cron = "0 * * * * ?")
    public void cancelTimeoutOrders() {
        // Redis 分布式锁：SETNX 防多实例重复执行
        var locked = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, "1", Duration.ofSeconds(50));
        if (Boolean.FALSE.equals(locked)) return;

        try {
            var deadline = LocalDateTime.now().minusMinutes(TIMEOUT_MINUTES);
            var orders = orderService.lambdaQuery()
                    .eq(OrderEntity::getStatus, "pending_pay")
                    .le(OrderEntity::getCreateTime, deadline)
                    .list();
            for (var order : orders) {
                order.setStatus("cancelled");
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("超时未支付，系统自动取消");
                orderService.updateById(order);
                log.info("订单 {} 超时自动取消", order.getOrderNo());
            }
        } catch (Exception e) {
            log.error("订单超时任务异常: {}", e.getMessage());
        }
    }
}
