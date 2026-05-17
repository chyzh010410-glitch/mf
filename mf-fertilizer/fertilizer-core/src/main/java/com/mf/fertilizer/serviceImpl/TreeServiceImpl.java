package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.constant.RedisKey;
import com.mf.fertilizer.entity.Tree;
import com.mf.fertilizer.mapper.TreeMapper;
import com.mf.fertilizer.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TreeServiceImpl extends ServiceImpl<TreeMapper, Tree> implements TreeService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<String> getCachedSpecies() {
        var key = RedisKey.TREE_LIST;
        var cached = redisTemplate.opsForList().range(key, 0, -1);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }
        var speciesList = lambdaQuery()
                .select(Tree::getSpecies)
                .groupBy(Tree::getSpecies)
                .list()
                .stream()
                .map(Tree::getSpecies)
                .distinct()
                .toList();
        if (!speciesList.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, speciesList);
            redisTemplate.expire(key, RedisKey.LIST_CACHE_MINUTES, TimeUnit.MINUTES);
        }
        return speciesList;
    }

    @Override
    public void evictCache() {
        redisTemplate.delete(RedisKey.TREE_LIST);
    }
}
