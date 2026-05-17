package com.mf.fertilizer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.fertilizer.entity.Tree;

import java.util.List;

public interface TreeService extends IService<Tree> {

    /** 获取缓存树种列表 */
    List<String> getCachedSpecies();

    void evictCache();
}
