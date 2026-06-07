package com.mf.server.controller;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.service.CommunityLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/client/likes") @RequiredArgsConstructor
public class LikeController {
    private final CommunityLikeService service;
    @GetMapping("/check") public ResultVO<?> check(@RequestParam String targetType, @RequestParam String targetId) {
        return ResultVO.success(service.check(UserContext.getUserId(), targetType, Long.valueOf(targetId)));
    }
    @PostMapping public ResultVO<?> toggle(@RequestBody Map<String,Object> b) {
        return ResultVO.success(service.toggle(UserContext.getUserId(), (String)b.get("targetType"), Long.valueOf(b.get("targetId").toString())));
    }
}
