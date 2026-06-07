package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.Product;
import java.util.List;

public interface ProductService extends IService<Product> {
    List<Product> getCachedList();
    void evictCache();
    boolean deductStock(Long productId, int quantity);
}
