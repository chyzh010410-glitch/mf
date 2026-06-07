package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.User;
import com.mf.server.service.PointsRecordService;
import com.mf.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/points") @RequiredArgsConstructor
public class PointsController {
    private final UserService userService;
    private final PointsRecordService pointsRecordService;

    @GetMapping public ResultVO<Integer> balance() {
        var u = userService.getById(UserContext.getUserId());
        return ResultVO.success(u != null ? u.getPoints() : 0);
    }
    @GetMapping("/records") public ResultVO<PageVO<?>> records(@ModelAttribute PageDTO p) {
        var pg = pointsRecordService.lambdaQuery()
                .eq(com.mf.server.entity.PointsRecord::getUserId, UserContext.getUserId())
                .orderByDesc(com.mf.server.entity.PointsRecord::getCreateTime)
                .page(new Page<>(p.getPage(), p.getSize()));
        return ResultVO.success(PageVO.of(pg.getTotal(), p.getPage(), p.getSize(), pg.getRecords()));
    }
}
