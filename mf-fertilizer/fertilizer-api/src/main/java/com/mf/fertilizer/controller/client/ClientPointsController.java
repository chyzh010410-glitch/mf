package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    private Long uid(jakarta.servlet.http.HttpServletRequest r) {
        return Long.valueOf(com.mf.fertilizer.util.JwtUtil.parse(r.getHeader("Authorization").substring(7)).getId());
    }

    @GetMapping
    public ResultVO<?> balance(jakarta.servlet.http.HttpServletRequest req) {
        var u = userService.getById(uid(req));
        return ResultVO.success(Map.of("points", u != null ? u.getPoints() : 0));
    }

    @GetMapping("/records")
    public ResultVO<PageVO<PointsRecord>> records(@ModelAttribute PageDTO page, jakarta.servlet.http.HttpServletRequest req) {
        var p = pointsRecordService.lambdaQuery().eq(PointsRecord::getUserId, uid(req))
                .orderByDesc(PointsRecord::getCreateTime)
                .page(new Page<>(page.getPage(), page.getSize()));
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }
}
