package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.CommunityComment;
import com.mf.fertilizer.mapper.CommunityCommentMapper;
import com.mf.fertilizer.service.CommunityCommentService;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommentServiceImpl extends ServiceImpl<CommunityCommentMapper, CommunityComment> implements CommunityCommentService {
}
