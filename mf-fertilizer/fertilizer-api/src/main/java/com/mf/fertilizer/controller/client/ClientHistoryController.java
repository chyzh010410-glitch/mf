package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.BrowsingHistory;
import com.mf.fertilizer.service.BrowsingHistoryService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/history")
@RequiredArgsConstructor
public class ClientHistoryController {
    private final BrowsingHistoryService service;
    private Long uid(jakarta.servlet.http.HttpServletRequest r) {
        return Long.valueOf(com.mf.fertilizer.util.JwtUtil.parse(r.getHeader("Authorization").substring(7)).getId());
    }

    @GetMapping
    public ResultVO<PageVO<BrowsingHistory>> list(@ModelAttribute PageDTO page, jakarta.servlet.http.HttpServletRequest req) {
        var p = service.lambdaQuery().eq(BrowsingHistory::getUserId, uid(req))
                .orderByDesc(BrowsingHistory::getCreateTime)
                .page(new Page<>(page.getPage(), page.getSize()));
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @DeleteMapping
    public ResultVO<?> clear(jakarta.servlet.http.HttpServletRequest req) {
        service.lambdaUpdate().eq(BrowsingHistory::getUserId, uid(req)).remove();
        return ResultVO.success();
    }
}
