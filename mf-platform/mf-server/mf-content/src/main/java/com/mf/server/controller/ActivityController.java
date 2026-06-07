package com.mf.server.controller;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.ActivityEntity;
import com.mf.server.service.ActivityEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/activities") @RequiredArgsConstructor
public class ActivityController {
    private final ActivityEntityService service;
    @GetMapping public ResultVO<?> list() {
        return ResultVO.success(service.lambdaQuery().eq(ActivityEntity::getStatus,"active").orderByAsc(ActivityEntity::getSortOrder).list());
    }
}
