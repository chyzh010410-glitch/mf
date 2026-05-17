package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.EncyclopediaArticle;
import com.mf.fertilizer.mapper.EncyclopediaArticleMapper;
import com.mf.fertilizer.service.EncyclopediaArticleService;
import org.springframework.stereotype.Service;

@Service
public class EncyclopediaArticleServiceImpl extends ServiceImpl<EncyclopediaArticleMapper, EncyclopediaArticle> implements EncyclopediaArticleService {
}
