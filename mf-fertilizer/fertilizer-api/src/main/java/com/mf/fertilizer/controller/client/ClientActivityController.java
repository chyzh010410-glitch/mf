package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.entity.ActivityEntity;
import com.mf.fertilizer.service.ActivityEntityService;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/client/activities")
@RequiredArgsConstructor
public class ClientActivityController {
    private final ActivityEntityService service;

    @GetMapping
    public ResultVO<List<ActivityEntity>> list() {
        return ResultVO.success(service.lambdaQuery().eq(ActivityEntity::getStatus, "active")
                .orderByDesc(ActivityEntity::getSortOrder).list());
    }

    @GetMapping("/{id}")
    public ResultVO<ActivityEntity> detail(@PathVariable Long id) {
        return ResultVO.success(service.getById(id));
    }
}
