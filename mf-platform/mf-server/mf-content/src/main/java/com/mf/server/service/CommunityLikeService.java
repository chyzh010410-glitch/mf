package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.CommunityLike;
import java.util.Map;

public interface CommunityLikeService extends IService<CommunityLike> {
    Map<String, Object> toggle(Long userId, String targetType, Long targetId);
    Map<String, Object> check(Long userId, String targetType, Long targetId);
}
