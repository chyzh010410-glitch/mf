package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.Message;
import com.mf.fertilizer.service.MessageService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/client/messages")
@RequiredArgsConstructor
public class ClientMessageController {
    private final MessageService service;

    private Long uid(jakarta.servlet.http.HttpServletRequest r) {
        return Long.valueOf(com.mf.fertilizer.util.JwtUtil.parse(
            r.getHeader("Authorization").substring(7)).getId());
    }

    @GetMapping
    public ResultVO<PageVO<Message>> list(@ModelAttribute PageDTO page,
                                           @RequestParam(name = "type", required = false) String type,
                                           jakarta.servlet.http.HttpServletRequest req) {
        var p = service.lambdaQuery().eq(Message::getUserId, uid(req))
                .eq(type != null, Message::getType, type)
                .orderByDesc(Message::getCreateTime)
                .page(new Page<>(page.getPage(), page.getSize()));
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/unread-count")
    public ResultVO<Long> unread(jakarta.servlet.http.HttpServletRequest req) {
        long count = service.lambdaQuery().eq(Message::getUserId, uid(req)).eq(Message::getIsRead, 0).count();
        return ResultVO.success(count);
    }

    @PutMapping("/{id}/read")
    public ResultVO<?> read(@PathVariable Long id) {
        var m = service.getById(id);
        if (m != null) { m.setIsRead(1); m.setReadTime(LocalDateTime.now()); service.updateById(m); }
        return ResultVO.success();
    }
}
