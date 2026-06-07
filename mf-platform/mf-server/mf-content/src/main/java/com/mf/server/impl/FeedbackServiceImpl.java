package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Feedback;
import com.mf.server.mapper.FeedbackMapper;
import com.mf.server.service.FeedbackService;
import org.springframework.stereotype.Service;

@Service public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {}
