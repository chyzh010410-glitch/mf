package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.Tree;
import com.mf.fertilizer.mapper.TreeMapper;
import com.mf.fertilizer.service.TreeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeServiceImpl extends ServiceImpl<TreeMapper, Tree> implements TreeService {

    @Override
    @Cacheable(value = "treeSpecies", key = "'all'")
    public List<String> getCachedSpecies() {
        return lambdaQuery()
                .select(Tree::getSpecies)
                .groupBy(Tree::getSpecies)
                .list()
                .stream()
                .map(Tree::getSpecies)
                .distinct()
                .toList();
    }

    @Override
    @CacheEvict(value = "treeSpecies", allEntries = true)
    public void evictCache() {
    }
}
