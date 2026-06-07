package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.EncyclopediaEntry;
import com.mf.server.mapper.EncyclopediaEntryMapper;
import com.mf.server.service.EncyclopediaEntryService;
import org.springframework.stereotype.Service;

@Service public class EncyclopediaEntryServiceImpl extends ServiceImpl<EncyclopediaEntryMapper, EncyclopediaEntry> implements EncyclopediaEntryService {}
