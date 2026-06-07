package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data @EqualsAndHashCode(callSuper=true) @TableName("fertilizer")
public class Fertilizer extends BaseEntity {
    private String name; private String type; private String brand;
    private String nutrientContent; private String unit; private BigDecimal stock;
    private BigDecimal unitPrice; private String remark;
}
