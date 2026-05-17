package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.dto.client.FeedbackSubmitDTO;
import com.mf.fertilizer.entity.Feedback;
import com.mf.fertilizer.service.FeedbackService;
import com.mf.fertilizer.vo.ResultVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/feedback")
@RequiredArgsConstructor
public class ClientFeedbackController {
    private final FeedbackService service;
    private Long uid(jakarta.servlet.http.HttpServletRequest r) {
        return Long.valueOf(com.mf.fertilizer.util.JwtUtil.parse(r.getHeader("Authorization").substring(7)).getId());
    }

    @PostMapping
    public ResultVO<?> submit(@Valid @RequestBody FeedbackSubmitDTO dto, jakarta.servlet.http.HttpServletRequest req) {
        var f = new Feedback();
        f.setUserId(uid(req)); f.setType(dto.getType()); f.setContent(dto.getContent());
        f.setContact(dto.getContact()); f.setStatus("pending");
        service.save(f);
        return ResultVO.success();
    }
}
