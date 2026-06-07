package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.ProductCategory;
import com.mf.server.mapper.ProductCategoryMapper;
import com.mf.server.service.ProductCategoryService;
import org.springframework.stereotype.Service;
@Service public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {}
