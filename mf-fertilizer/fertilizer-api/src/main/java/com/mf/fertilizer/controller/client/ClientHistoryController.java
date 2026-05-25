package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.context.UserContext;
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

    @GetMapping
    public ResultVO<PageVO<BrowsingHistory>> list(@ModelAttribute PageDTO page) {
        var p = service.lambdaQuery().eq(BrowsingHistory::getUserId, UserContext.getUserId())
                .orderByDesc(BrowsingHistory::getCreateTime)
                .page(new Page<>(page.getPage(), page.getSize()));
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @DeleteMapping
    public ResultVO<?> clear() {
        service.lambdaUpdate().eq(BrowsingHistory::getUserId, UserContext.getUserId()).remove();
        return ResultVO.success();
    }
}
