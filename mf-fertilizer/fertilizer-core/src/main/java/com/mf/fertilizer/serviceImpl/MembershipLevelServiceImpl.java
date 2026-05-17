package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.MembershipLevel;
import com.mf.fertilizer.mapper.MembershipLevelMapper;
import com.mf.fertilizer.service.MembershipLevelService;
import org.springframework.stereotype.Service;

@Service
public class MembershipLevelServiceImpl extends ServiceImpl<MembershipLevelMapper, MembershipLevel> implements MembershipLevelService {
}
