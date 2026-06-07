package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.CommunityComment;
import com.mf.server.mapper.CommunityCommentMapper;
import com.mf.server.service.CommunityCommentService;
import org.springframework.stereotype.Service;

@Service public class CommunityCommentServiceImpl extends ServiceImpl<CommunityCommentMapper, CommunityComment> implements CommunityCommentService {}
