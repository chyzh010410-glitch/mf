package com.mf.fertilizer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("points_record")
public class PointsRecord extends BaseEntity {
    private Long userId;
    private Integer points;
    private String type;
    private String description;
    private Long refId;
    private Integer balanceAfter;
}
