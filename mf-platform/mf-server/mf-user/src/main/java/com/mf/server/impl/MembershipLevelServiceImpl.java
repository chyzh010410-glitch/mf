package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.MembershipLevel;
import com.mf.server.mapper.MembershipLevelMapper;
import com.mf.server.service.MembershipLevelService;
import org.springframework.stereotype.Service;
@Service public class MembershipLevelServiceImpl extends ServiceImpl<MembershipLevelMapper, MembershipLevel> implements MembershipLevelService {}
