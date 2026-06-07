package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.CommunityComment;
import com.mf.server.service.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/comments") @RequiredArgsConstructor
public class CommentController {
    private final CommunityCommentService service;
    @GetMapping public ResultVO<PageVO<CommunityComment>> list(@ModelAttribute PageDTO p, @RequestParam String targetType, @RequestParam String targetId) {
        var w=new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<CommunityComment>()
                .eq(CommunityComment::getTargetType,targetType).eq(CommunityComment::getTargetId,Long.valueOf(targetId))
                .eq(CommunityComment::getParentId,0L).eq(CommunityComment::getIsDeletedByAdmin,0)
                .orderByDesc(CommunityComment::getCreateTime);
        var pg=service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @PostMapping public ResultVO<?> post(@RequestBody CommunityComment c) {
        c.setUserId(UserContext.getUserId()); service.save(c); return ResultVO.success(); }
    @GetMapping("/{id}/replies") public ResultVO<?> replies(@PathVariable Long id) {
        return ResultVO.success(service.lambdaQuery().eq(CommunityComment::getParentId,id).orderByAsc(CommunityComment::getCreateTime).list()); }
}
