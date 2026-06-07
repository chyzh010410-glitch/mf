package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Message;
import com.mf.server.mapper.MessageMapper;
import com.mf.server.service.MessageService;
import org.springframework.stereotype.Service;

@Service public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {}
