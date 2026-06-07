package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.OrderEntity;
import java.util.Map;

public interface OrderEntityService extends IService<OrderEntity> {
    Map<String,Object> createOrder(Long userId, java.util.List<Map<String,Object>> items);
}
