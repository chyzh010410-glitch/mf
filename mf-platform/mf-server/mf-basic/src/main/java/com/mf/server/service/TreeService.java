package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.Tree;
import java.util.List;

public interface TreeService extends IService<Tree> {
    List<String> getCachedSpecies();
    void evictCache();
}
