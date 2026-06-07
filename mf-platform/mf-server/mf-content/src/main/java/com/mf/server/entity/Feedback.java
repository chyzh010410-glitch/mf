package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("feedback")
public class Feedback extends BaseEntity {
    Long userId; String contact; String content; String images; String type; String status; Long handlerId; String handlerReply; java.time.LocalDateTime handleTime;
}
