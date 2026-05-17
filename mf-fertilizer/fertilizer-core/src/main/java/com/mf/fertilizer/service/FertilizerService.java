package com.mf.fertilizer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.fertilizer.entity.Fertilizer;

import java.util.List;

public interface FertilizerService extends IService<Fertilizer> {

    /** 获取缓存肥料列表 */
    List<Fertilizer> getCachedList();

    void evictCache();
}
