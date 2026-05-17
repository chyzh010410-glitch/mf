package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.Message;
import com.mf.fertilizer.mapper.MessageMapper;
import com.mf.fertilizer.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}
