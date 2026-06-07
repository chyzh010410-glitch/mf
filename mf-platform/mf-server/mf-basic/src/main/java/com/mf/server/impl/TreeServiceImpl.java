package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Tree;
import com.mf.server.mapper.TreeMapper;
import com.mf.server.service.TreeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TreeServiceImpl extends ServiceImpl<TreeMapper, Tree> implements TreeService {
    @Override @Cacheable(value="treeSpecies",key="'all'")
    public List<String> getCachedSpecies() {
        return lambdaQuery().select(Tree::getSpecies).groupBy(Tree::getSpecies).list().stream().map(Tree::getSpecies).distinct().toList();
    }
    @Override @CacheEvict(value="treeSpecies",allEntries=true)
    public void evictCache() {}
}
