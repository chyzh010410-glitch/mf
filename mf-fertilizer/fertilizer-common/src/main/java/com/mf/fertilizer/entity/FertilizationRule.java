package com.mf.fertilizer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fertilization_rule")
public class FertilizationRule extends BaseEntity {

    private String species;

    private Integer ageMin;

    private Integer ageMax;

    /** spring / summer / autumn / winter / all */
    private String season;

    private Long fertilizerId;

    private BigDecimal recommendAmount;

    private String method;

    /** 越大越优先 */
    private Integer priority;

    private String remark;
}
