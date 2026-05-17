package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.CommunityLike;
import com.mf.fertilizer.mapper.CommunityLikeMapper;
import com.mf.fertilizer.service.CommunityLikeService;
import org.springframework.stereotype.Service;

@Service
public class CommunityLikeServiceImpl extends ServiceImpl<CommunityLikeMapper, CommunityLike> implements CommunityLikeService {
}
