package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Product;
import com.mf.server.mapper.ProductMapper;
import com.mf.server.service.ProductService;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    private final RedissonClient redisson;
    public ProductServiceImpl(RedissonClient redisson) { this.redisson = redisson; }

    @Override @Cacheable(value="fertilizerList",key="'all'")
    public List<Product> getCachedList() { return lambdaQuery().list(); }

    @Override @CacheEvict(value="fertilizerList",allEntries=true)
    public void evictCache() {}

    @Override
    public boolean deductStock(Long productId, int quantity) {
        var lock = redisson.getLock("stock:product:" + productId);
        try {
            if (!lock.tryLock(0, 10, TimeUnit.SECONDS)) return false;
            var p = getById(productId);
            if (p == null || p.getStock() < quantity) return false;
            p.setStock(p.getStock() - quantity);
            p.setSalesCount(p.getSalesCount() + quantity);
            updateById(p);
            return true;
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); return false;
        } finally { if (lock.isHeldByCurrentThread()) lock.unlock(); }
    }
}
