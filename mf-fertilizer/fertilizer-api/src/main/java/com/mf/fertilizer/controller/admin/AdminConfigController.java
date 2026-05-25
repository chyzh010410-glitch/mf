package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.PlatformConfigSaveDTO;
import com.mf.fertilizer.entity.PlatformConfig;
import com.mf.fertilizer.service.PlatformConfigService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/configs")
@RequiredArgsConstructor
public class AdminConfigController {

    private final PlatformConfigService configService;

    @GetMapping
    public ResultVO<PageVO<PlatformConfig>> list(@ModelAttribute PageDTO page,
                                                   @RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) String configGroup) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PlatformConfig>()
                .like(StrUtil.isNotBlank(keyword), PlatformConfig::getConfigKey, keyword)
                .eq(StrUtil.isNotBlank(configGroup), PlatformConfig::getConfigGroup, configGroup)
                .orderByAsc(PlatformConfig::getConfigGroup)
                .orderByAsc(PlatformConfig::getConfigKey);
        var p = configService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<PlatformConfig> detail(@PathVariable Long id) {
        var c = configService.getById(id);
        if (c == null) return ResultVO.fail(404, "配置不存在");
        return ResultVO.success(c);
    }

    @PostMapping
    @OperationLog(module = "平台配置", action = "新增")
    public ResultVO<?> save(@RequestBody PlatformConfigSaveDTO dto) {
        var c = new PlatformConfig();
        BeanUtils.copyProperties(dto, c);
        configService.save(c);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "平台配置", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody PlatformConfigSaveDTO dto) {
        var c = configService.getById(id);
        if (c == null) return ResultVO.fail(404, "配置不存在");
        BeanUtils.copyProperties(dto, c);
        c.setId(id);
        configService.updateById(c);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "平台配置", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        configService.removeById(id);
        return ResultVO.success();
    }
}
