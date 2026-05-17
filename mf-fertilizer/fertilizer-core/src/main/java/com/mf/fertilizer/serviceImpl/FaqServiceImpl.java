package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.Faq;
import com.mf.fertilizer.mapper.FaqMapper;
import com.mf.fertilizer.service.FaqService;
import org.springframework.stereotype.Service;

@Service
public class FaqServiceImpl extends ServiceImpl<FaqMapper, Faq> implements FaqService {
}
