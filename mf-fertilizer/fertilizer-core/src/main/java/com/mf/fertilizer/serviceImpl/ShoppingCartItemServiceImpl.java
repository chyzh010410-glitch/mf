package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.ShoppingCartItem;
import com.mf.fertilizer.mapper.ShoppingCartItemMapper;
import com.mf.fertilizer.service.ShoppingCartItemService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartItemServiceImpl extends ServiceImpl<ShoppingCartItemMapper, ShoppingCartItem> implements ShoppingCartItemService {
}
