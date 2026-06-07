package com.mf.server.controller;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.Feedback;
import com.mf.server.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/feedback") @RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService service;
    @PostMapping public ResultVO<?> submit(@RequestBody Feedback fb) { fb.setUserId(UserContext.getUserId()); service.save(fb); return ResultVO.success(); }
}
