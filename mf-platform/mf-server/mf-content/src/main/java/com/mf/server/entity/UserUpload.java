package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("user_upload")
public class UserUpload extends BaseEntity {
    Long userId; String name; String description; String images; String location; String tags; String status; Integer isPublished; String reviewComment; Long reviewerId; java.time.LocalDateTime reviewTime;
}
