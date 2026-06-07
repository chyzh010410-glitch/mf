package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.UserAddress;
import com.mf.server.mapper.UserAddressMapper;
import com.mf.server.service.UserAddressService;
import org.springframework.stereotype.Service;

@Service public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {}
