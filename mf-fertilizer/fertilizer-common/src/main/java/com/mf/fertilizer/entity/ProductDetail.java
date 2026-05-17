package com.mf.fertilizer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_detail")
public class ProductDetail extends BaseEntity {
    private Long productId;
    private String detailType;
    private String attrsJson;
}
