package com.mf.fertilizer.serviceImpl;

import com.mf.fertilizer.entity.Message;
import com.mf.fertilizer.service.MessageService;
import com.mf.fertilizer.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MessageService messageService;

    @Override
    @Async("taskExecutor")
    public void sendOrderCreatedNotification(Long userId, String orderNo) {
        var msg = new Message();
        msg.setUserId(userId);
        msg.setTitle("订单已创建");
        msg.setContent("您的订单 " + orderNo + " 已创建成功，请尽快完成支付。");
        msg.setType("order");
        msg.setPushChannel("system");
        msg.setIsRead(0);
        messageService.save(msg);
        log.info("异步发送订单通知成功: userId={}, orderNo={}", userId, orderNo);
    }
}
