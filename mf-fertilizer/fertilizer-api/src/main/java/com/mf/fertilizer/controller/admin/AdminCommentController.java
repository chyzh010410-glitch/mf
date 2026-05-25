package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.CommunityComment;
import com.mf.fertilizer.service.CommunityCommentService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommunityCommentService commentService;

    @GetMapping
    public ResultVO<PageVO<CommunityComment>> list(@ModelAttribute PageDTO page,
                                                    @RequestParam(required = false) String targetType,
                                                    @RequestParam(required = false) String keyword,
                                                    @RequestParam(required = false) Integer isDeleted) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<CommunityComment>()
                .eq(StrUtil.isNotBlank(targetType), CommunityComment::getTargetType, targetType)
                .eq(isDeleted != null, CommunityComment::getIsDeletedByAdmin, isDeleted)
                .like(StrUtil.isNotBlank(keyword), CommunityComment::getContent, keyword)
                .orderByDesc(CommunityComment::getCreateTime);
        var p = commentService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PutMapping("/{id}/hide")
    @OperationLog(module = "评论管理", action = "屏蔽")
    public ResultVO<?> hide(@PathVariable Long id) {
        var comment = commentService.getById(id);
        if (comment == null) return ResultVO.fail(404, "评论不存在");
        comment.setIsDeletedByAdmin(1);
        commentService.updateById(comment);
        return ResultVO.success();
    }

    @PutMapping("/{id}/restore")
    @OperationLog(module = "评论管理", action = "恢复")
    public ResultVO<?> restore(@PathVariable Long id) {
        var comment = commentService.getById(id);
        if (comment == null) return ResultVO.fail(404, "评论不存在");
        comment.setIsDeletedByAdmin(0);
        commentService.updateById(comment);
        return ResultVO.success();
    }
}
