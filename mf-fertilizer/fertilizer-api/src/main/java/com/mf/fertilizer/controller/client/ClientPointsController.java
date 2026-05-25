package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.PointsRecord;
import com.mf.fertilizer.service.PointsRecordService;
import com.mf.fertilizer.service.UserService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/client/points")
@RequiredArgsConstructor
public class ClientPointsController {
    private final PointsRecordService pointsRecordService;
    private final UserService userService;

    @GetMapping
    public ResultVO<?> balance() {
        var u = userService.getById(UserContext.getUserId());
        return ResultVO.success(Map.of("points", u != null ? u.getPoints() : 0));
    }

    @GetMapping("/records")
    public ResultVO<PageVO<PointsRecord>> records(@ModelAttribute PageDTO page) {
        var p = pointsRecordService.lambdaQuery().eq(PointsRecord::getUserId, UserContext.getUserId())
                .orderByDesc(PointsRecord::getCreateTime)
                .page(new Page<>(page.getPage(), page.getSize()));
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }
}
