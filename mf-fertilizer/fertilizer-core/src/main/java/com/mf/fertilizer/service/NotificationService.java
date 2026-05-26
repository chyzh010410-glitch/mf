package com.mf.fertilizer.service;

public interface NotificationService {
    void sendOrderCreatedNotification(Long userId, String orderNo);
}
