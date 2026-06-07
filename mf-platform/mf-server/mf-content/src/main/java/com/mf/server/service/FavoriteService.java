package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.Favorite;

public interface FavoriteService extends IService<Favorite> {
    Favorite add(Long userId, String targetType, Long targetId);
}
