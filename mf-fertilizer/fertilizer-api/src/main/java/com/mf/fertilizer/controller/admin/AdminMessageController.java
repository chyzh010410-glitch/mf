package com.mf.fertilizer.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.MessageSendDTO;
import com.mf.fertilizer.entity.Message;
import com.mf.fertilizer.service.MessageService;
import com.mf.fertilizer.service.UserService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/messages")
@RequiredArgsConstructor
public class AdminMessageController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping
    public ResultVO<PageVO<Message>> list(@ModelAttribute PageDTO page) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Message>()
                .orderByDesc(Message::getCreateTime);
        var p = messageService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PostMapping
    @OperationLog(module = "消息推送", action = "发送")
    public ResultVO<?> send(@RequestBody MessageSendDTO dto) {
        if (dto.getUserIds() == null || dto.getUserIds().isEmpty()) {
            return ResultVO.fail(400, "请指定推送用户");
        }
        List<Long> notFound = new ArrayList<>();
        int success = 0;
        for (Long userId : dto.getUserIds()) {
            if (userService.getById(userId) == null) {
                notFound.add(userId);
                continue;
            }
            var msg = new Message();
            msg.setUserId(userId);
            msg.setTitle(dto.getTitle());
            msg.setContent(dto.getContent());
            msg.setType(dto.getType());
            msg.setPushChannel(dto.getPushChannel());
            msg.setIsRead(0);
            messageService.save(msg);
            success++;
        }
        if (!notFound.isEmpty()) {
            return ResultVO.fail(400, "以下用户不存在: " + notFound);
        }
        return ResultVO.success();
    }
}
