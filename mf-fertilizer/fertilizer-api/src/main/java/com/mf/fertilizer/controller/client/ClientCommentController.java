package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.CommunityComment;
import com.mf.fertilizer.service.CommunityCommentService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client/comments")
@RequiredArgsConstructor
public class ClientCommentController {

    private final CommunityCommentService commentService;

    @GetMapping
    public ResultVO<PageVO<CommunityComment>> list(@ModelAttribute PageDTO page,
                                                    @RequestParam String targetType,
                                                    @RequestParam String targetId) {
        var p = commentService.lambdaQuery()
                .eq(CommunityComment::getTargetType, targetType)
                .eq(CommunityComment::getTargetId, targetId)
                .eq(CommunityComment::getParentId, 0L)
                .eq(CommunityComment::getIsDeletedByAdmin, 0)
                .orderByDesc(CommunityComment::getCreateTime)
                .page(new Page<>(page.getPage(), page.getSize()));
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}/replies")
    public ResultVO<?> replies(@PathVariable Long id) {
        var replies = commentService.lambdaQuery()
                .eq(CommunityComment::getParentId, id)
                .eq(CommunityComment::getIsDeletedByAdmin, 0)
                .orderByAsc(CommunityComment::getCreateTime)
                .list();
        return ResultVO.success(replies);
    }

    @PostMapping
    public ResultVO<?> post(@RequestBody Map<String, Object> body) {
        var comment = new CommunityComment();
        comment.setUserId(UserContext.getUserId());
        comment.setTargetType((String) body.get("targetType"));
        comment.setTargetId(Long.valueOf(body.get("targetId").toString()));
        comment.setContent((String) body.get("content"));
        if (body.get("parentId") != null) comment.setParentId(Long.valueOf(body.get("parentId").toString()));
        if (body.get("replyToUserId") != null) comment.setReplyToUserId(Long.valueOf(body.get("replyToUserId").toString()));
        comment.setIsDeletedByAdmin(0);
        comment.setLikeCount(0);
        commentService.save(comment);
        return ResultVO.success(comment);
    }
}
