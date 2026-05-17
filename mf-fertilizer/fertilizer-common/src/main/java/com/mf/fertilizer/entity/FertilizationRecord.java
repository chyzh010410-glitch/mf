package com.mf.fertilizer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fertilization_record")
public class FertilizationRecord extends BaseEntity {

    private Long treeId;

    private Long fertilizerId;

    private BigDecimal amount;

    private LocalDate fertilizeDate;

    private Long operatorId;

    /** broadcast / furrow / foliar / drip */
    private String method;

    private String remark;
}
