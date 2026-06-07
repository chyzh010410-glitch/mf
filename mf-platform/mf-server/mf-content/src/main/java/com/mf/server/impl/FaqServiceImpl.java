package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Faq;
import com.mf.server.mapper.FaqMapper;
import com.mf.server.service.FaqService;
import org.springframework.stereotype.Service;

@Service public class FaqServiceImpl extends ServiceImpl<FaqMapper, Faq> implements FaqService {}
