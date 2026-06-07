package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.EncyclopediaArticle;
import com.mf.server.mapper.EncyclopediaArticleMapper;
import com.mf.server.service.EncyclopediaArticleService;
import org.springframework.stereotype.Service;

@Service public class EncyclopediaArticleServiceImpl extends ServiceImpl<EncyclopediaArticleMapper, EncyclopediaArticle> implements EncyclopediaArticleService {}
