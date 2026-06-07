package com.mf.server.task;

import com.mf.server.entity.OrderEntity;
import com.mf.server.service.OrderEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j @Component @RequiredArgsConstructor
public class OrderTimeoutTask {
    private final OrderEntityService orderService;
    private final RedissonClient redissonClient;
    private static final long TIMEOUT_MINUTES = 30;

    @Scheduled(cron = "0 * * * * ?")
    public void cancelTimeoutOrders() {
        RLock lock = redissonClient.getLock("task:order_timeout:lock");
        try {
            if (!lock.tryLock(0, 50, TimeUnit.SECONDS)) return;
            var deadline = LocalDateTime.now().minusMinutes(TIMEOUT_MINUTES);
            var orders = orderService.lambdaQuery()
                    .eq(OrderEntity::getStatus, "pending_pay")
                    .le(OrderEntity::getCreateTime, deadline).list();
            for (var o : orders) {
                o.setStatus("cancelled"); o.setCancelTime(LocalDateTime.now());
                o.setCancelReason("超时未支付，系统自动取消"); orderService.updateById(o);
                log.info("订单 {} 超时自动取消", o.getOrderNo());
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt();
        } finally { if (lock.isHeldByCurrentThread()) lock.unlock(); }
    }
}
