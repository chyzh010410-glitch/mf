package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.BrowsingHistory;
import com.mf.fertilizer.mapper.BrowsingHistoryMapper;
import com.mf.fertilizer.service.BrowsingHistoryService;
import org.springframework.stereotype.Service;

@Service
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory> implements BrowsingHistoryService {
}
