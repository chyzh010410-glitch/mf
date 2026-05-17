package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.OrderEntity;
import com.mf.fertilizer.mapper.OrderEntityMapper;
import com.mf.fertilizer.service.OrderEntityService;
import org.springframework.stereotype.Service;

@Service
public class OrderEntityServiceImpl extends ServiceImpl<OrderEntityMapper, OrderEntity> implements OrderEntityService {
}
