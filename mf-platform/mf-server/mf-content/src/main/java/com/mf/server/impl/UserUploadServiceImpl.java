package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.UserUpload;
import com.mf.server.mapper.UserUploadMapper;
import com.mf.server.service.UserUploadService;
import org.springframework.stereotype.Service;

@Service public class UserUploadServiceImpl extends ServiceImpl<UserUploadMapper, UserUpload> implements UserUploadService {}
