package com.mf.fertilizer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsVO implements Serializable {

    /** 总施肥次数 */
    private Long totalRecords;

    /** 施肥总面积(平方米) */
    private BigDecimal totalArea;

    /** 肥料总用量(kg) */
    private BigDecimal totalAmount;

    /** 涉及树木数量 */
    private Long treeCount;

    /** 涉及肥料种类数 */
    private Long fertilizerTypeCount;
}
