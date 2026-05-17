package com.mf.fertilizer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shopping_cart_item")
public class ShoppingCartItem extends BaseEntity {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Integer selected;
}
