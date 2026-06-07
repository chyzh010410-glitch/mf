package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("browsing_history")
public class BrowsingHistory extends BaseEntity {
    Long userId; String targetType; Long targetId; String title;
}
