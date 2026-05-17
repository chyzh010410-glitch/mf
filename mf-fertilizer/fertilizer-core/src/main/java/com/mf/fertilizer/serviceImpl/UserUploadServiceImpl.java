package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.UserUpload;
import com.mf.fertilizer.mapper.UserUploadMapper;
import com.mf.fertilizer.service.UserUploadService;
import org.springframework.stereotype.Service;

@Service
public class UserUploadServiceImpl extends ServiceImpl<UserUploadMapper, UserUpload> implements UserUploadService {
}
