package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.EncyclopediaEntry;
import com.mf.fertilizer.mapper.EncyclopediaEntryMapper;
import com.mf.fertilizer.service.EncyclopediaEntryService;
import org.springframework.stereotype.Service;

@Service
public class EncyclopediaEntryServiceImpl extends ServiceImpl<EncyclopediaEntryMapper, EncyclopediaEntry> implements EncyclopediaEntryService {
}
