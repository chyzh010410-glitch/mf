package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("encyclopedia_entry")
public class EncyclopediaEntry extends BaseEntity {
    String name; String scientificName; String alias; String pinyin; String family; String genus; Long categoryId; String coverImage; String images; String description; String morphology; String distribution; String habitat; String careGuide; String valueDescription; Integer isPublished; Integer viewCount; Integer likeCount; Integer commentCount; String tags;
}
