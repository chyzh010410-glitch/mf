package com.mf.server.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.FertilizationRule;
import com.mf.server.service.FertilizationRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/rule") @RequiredArgsConstructor
public class FertilizationRuleController {
    private final FertilizationRuleService service;

    @GetMapping public ResultVO<PageVO<FertilizationRule>> list(@ModelAttribute PageDTO p) {
        var pg = service.page(new Page<>(p.getPage(),p.getSize()),new LambdaQueryWrapper<FertilizationRule>().orderByDesc(FertilizationRule::getCreateTime));
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @GetMapping("/{id}") public ResultVO<FertilizationRule> detail(@PathVariable Long id) { return ResultVO.success(service.getById(id)); }
    @PostMapping public ResultVO<?> save(@RequestBody FertilizationRule r) { service.save(r); return ResultVO.success(); }
    @PutMapping public ResultVO<?> update(@RequestBody FertilizationRule r) { service.updateById(r); return ResultVO.success(); }
    @DeleteMapping("/{id}") public ResultVO<?> delete(@PathVariable Long id) { service.removeById(id); return ResultVO.success(); }
}
