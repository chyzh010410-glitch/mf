package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.Message;
import com.mf.server.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController @RequestMapping("/client/messages") @RequiredArgsConstructor
public class MessageController {
    private final MessageService service;
    @GetMapping public ResultVO<PageVO<Message>> list(@ModelAttribute PageDTO p) {
        var pg=service.lambdaQuery().eq(Message::getUserId,UserContext.getUserId()).orderByDesc(Message::getCreateTime).page(new Page<>(p.getPage(),p.getSize()));
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @GetMapping("/unread-count") public ResultVO<Long> unread() {
        return ResultVO.success(service.lambdaQuery().eq(Message::getUserId,UserContext.getUserId()).eq(Message::getIsRead,0).count());
    }
    @PutMapping("/{id}/read") public ResultVO<?> read(@PathVariable Long id) {
        var m=service.getById(id); if(m!=null) { m.setIsRead(1); m.setReadTime(LocalDateTime.now()); service.updateById(m); }
        return ResultVO.success();
    }
}
