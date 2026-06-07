package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.OrderItem;
import com.mf.server.mapper.OrderItemMapper;
import com.mf.server.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {}
