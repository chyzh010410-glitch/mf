package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.context.UserContext;
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

    @PostMapping
    public ResultVO<?> submit(@Valid @RequestBody FeedbackSubmitDTO dto) {
        var f = new Feedback();
        f.setUserId(UserContext.getUserId()); f.setType(dto.getType()); f.setContent(dto.getContent());
        f.setContact(dto.getContact()); f.setStatus("pending");
        service.save(f);
        return ResultVO.success();
    }
}
