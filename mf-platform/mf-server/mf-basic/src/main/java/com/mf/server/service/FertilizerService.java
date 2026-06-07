package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.Fertilizer;
import java.util.List;

public interface FertilizerService extends IService<Fertilizer> {
    List<Fertilizer> getCachedList();
    void evictCache();
}
