package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.ProductDetail;
import com.mf.server.mapper.ProductDetailMapper;
import com.mf.server.service.ProductDetailService;
import org.springframework.stereotype.Service;
@Service public class ProductDetailServiceImpl extends ServiceImpl<ProductDetailMapper, ProductDetail> implements ProductDetailService {}
