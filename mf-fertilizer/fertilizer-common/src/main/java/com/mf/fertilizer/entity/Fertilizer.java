package com.mf.fertilizer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fertilizer")
public class Fertilizer extends BaseEntity {

    private String name;

    /** organic / compound / potash / nitrogen / phosphate */
    private String type;

    private String brand;

    private String nutrientContent;

    private String unit;

    private BigDecimal stock;

    private BigDecimal unitPrice;

    private String remark;
}
