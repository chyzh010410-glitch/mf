package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.Feedback;
import com.mf.fertilizer.service.FeedbackService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/admin/feedbacks")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public ResultVO<PageVO<Feedback>> list(@ModelAttribute PageDTO page,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(required = false) String type) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Feedback>()
                .eq(StrUtil.isNotBlank(status), Feedback::getStatus, status)
                .eq(StrUtil.isNotBlank(type), Feedback::getType, type)
                .orderByDesc(Feedback::getCreateTime);
        var p = feedbackService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PutMapping("/{id}/reply")
    @OperationLog(module = "反馈处理", action = "回复")
    public ResultVO<?> reply(@PathVariable Long id, @RequestBody Map<String, String> body) {
        var fb = feedbackService.getById(id);
        if (fb == null) return ResultVO.fail(404, "反馈不存在");
        fb.setHandlerReply(body.get("reply"));
        fb.setHandlerId(com.mf.fertilizer.context.UserContext.getUserId());
        fb.setHandleTime(LocalDateTime.now());
        fb.setStatus("handled");
        feedbackService.updateById(fb);
        return ResultVO.success();
    }
}
