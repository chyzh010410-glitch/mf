package com.mf.fertilizer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mf.fertilizer.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
