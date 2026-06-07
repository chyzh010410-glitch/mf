package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("message")
public class Message extends BaseEntity {
    Long userId; String title; String content; String type; String targetType; Long targetId; Integer isRead; java.time.LocalDateTime readTime; String pushChannel;
}
