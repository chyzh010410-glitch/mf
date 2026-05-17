package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.ProductDetail;
import com.mf.fertilizer.mapper.ProductDetailMapper;
import com.mf.fertilizer.service.ProductDetailService;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailServiceImpl extends ServiceImpl<ProductDetailMapper, ProductDetail> implements ProductDetailService {
}
