package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("encyclopedia_article")
public class EncyclopediaArticle extends BaseEntity {
    String title; String summary; String coverImage; String content; Long authorId; Long categoryId; String tags; Integer isPublished; Integer isTop; Integer isRecommend; Integer viewCount; Integer likeCount; Integer commentCount;
}
