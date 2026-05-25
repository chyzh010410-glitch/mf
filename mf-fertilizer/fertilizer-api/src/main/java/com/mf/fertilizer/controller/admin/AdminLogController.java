package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.SystemLog;
import com.mf.fertilizer.service.SystemLogService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/logs")
@RequiredArgsConstructor
public class AdminLogController {

    private final SystemLogService logService;

    @GetMapping
    public ResultVO<PageVO<SystemLog>> list(@ModelAttribute PageDTO page,
                                             @RequestParam(required = false) String module,
                                             @RequestParam(required = false) String keyword) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SystemLog>()
                .eq(StrUtil.isNotBlank(module), SystemLog::getModule, module)
                .and(StrUtil.isNotBlank(keyword), q -> q
                        .like(SystemLog::getOperatorName, keyword).or()
                        .like(SystemLog::getAction, keyword).or()
                        .like(SystemLog::getTarget, keyword))
                .orderByDesc(SystemLog::getCreateTime);
        var p = logService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }
}
