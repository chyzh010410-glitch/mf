package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Payment;
import com.mf.server.mapper.PaymentMapper;
import com.mf.server.service.PaymentService;
import org.springframework.stereotype.Service;

@Service public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {}
