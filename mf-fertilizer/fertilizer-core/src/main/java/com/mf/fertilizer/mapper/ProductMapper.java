package com.mf.fertilizer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mf.fertilizer.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
