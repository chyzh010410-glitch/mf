package com.mf.fertilizer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mf.fertilizer.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
