package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.FileUpload;
import com.mf.fertilizer.mapper.FileUploadMapper;
import com.mf.fertilizer.service.FileUploadService;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadMapper, FileUpload> implements FileUploadService {
}
