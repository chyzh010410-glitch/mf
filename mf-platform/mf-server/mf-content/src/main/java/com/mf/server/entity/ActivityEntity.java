package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("activity")
public class ActivityEntity extends BaseEntity {
    String title; String description; String coverImage; String type; String ruleJson; java.time.LocalDateTime startTime; java.time.LocalDateTime endTime; String status; Integer isBanner; Integer sortOrder;
}
