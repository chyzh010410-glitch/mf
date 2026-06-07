package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.SystemLog;
import com.mf.server.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/admin/logs") @RequiredArgsConstructor
public class SystemLogController {
    private final SystemLogService service;

    @GetMapping public ResultVO<PageVO<SystemLog>> list(@ModelAttribute PageDTO p, @RequestParam(required=false) String module) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SystemLog>()
                .eq(module!=null,SystemLog::getModule,module).orderByDesc(SystemLog::getCreateTime);
        var pg = service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
}
