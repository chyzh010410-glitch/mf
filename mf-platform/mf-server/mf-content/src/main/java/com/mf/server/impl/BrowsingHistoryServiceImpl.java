package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.BrowsingHistory;
import com.mf.server.mapper.BrowsingHistoryMapper;
import com.mf.server.service.BrowsingHistoryService;
import org.springframework.stereotype.Service;

@Service public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory> implements BrowsingHistoryService {}
