package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("community_comment")
public class CommunityComment extends BaseEntity {
    private Long userId; private String targetType; private Long targetId;
    private Long parentId; private String content; private Integer likeCount;
    private Integer isDeletedByAdmin; private Long replyToUserId; private String ip;
}
