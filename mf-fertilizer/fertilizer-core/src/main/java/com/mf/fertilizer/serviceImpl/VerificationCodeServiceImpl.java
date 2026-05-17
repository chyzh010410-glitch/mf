package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.VerificationCode;
import com.mf.fertilizer.mapper.VerificationCodeMapper;
import com.mf.fertilizer.service.VerificationCodeService;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeServiceImpl extends ServiceImpl<VerificationCodeMapper, VerificationCode> implements VerificationCodeService {
}
