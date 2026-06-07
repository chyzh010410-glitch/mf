package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.BrowsingHistory;
import com.mf.server.service.BrowsingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/history") @RequiredArgsConstructor
public class HistoryController {
    private final BrowsingHistoryService service;

    @GetMapping public ResultVO<PageVO<BrowsingHistory>> list(@ModelAttribute PageDTO p) {
        var pg = service.lambdaQuery().eq(BrowsingHistory::getUserId, UserContext.getUserId())
                .orderByDesc(BrowsingHistory::getCreateTime).page(new Page<>(p.getPage(), p.getSize()));
        return ResultVO.success(PageVO.of(pg.getTotal(), p.getPage(), p.getSize(), pg.getRecords()));
    }
    @DeleteMapping public ResultVO<?> clear() {
        service.lambdaUpdate().eq(BrowsingHistory::getUserId, UserContext.getUserId()).remove();
        return ResultVO.success();
    }
}
