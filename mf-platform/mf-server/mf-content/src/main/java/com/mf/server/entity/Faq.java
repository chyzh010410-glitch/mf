package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("faq")
public class Faq extends BaseEntity {
    String question; String answer; String category; Integer sortOrder; Integer isPublished; Integer viewCount;
}
