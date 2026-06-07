package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.ShoppingCartItem;
import java.util.List;

public interface ShoppingCartItemService extends IService<ShoppingCartItem> {
    List<ShoppingCartItem> listByUser(Long userId);
    void restoreOrAdd(Long userId, Long productId, int quantity);
    void removeByUser(Long userId, Long productId);
}
