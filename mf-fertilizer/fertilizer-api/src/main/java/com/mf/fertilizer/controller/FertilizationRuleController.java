package com.mf.fertilizer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.FertilizationRuleDTO;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.RecommendRequestDTO;
import com.mf.fertilizer.entity.FertilizationRule;
import com.mf.fertilizer.service.FertilizationRuleService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
public class FertilizationRuleController {

    private final FertilizationRuleService ruleService;

    @GetMapping("/page")
    public ResultVO<PageVO<FertilizationRule>> page(@Valid PageDTO dto) {
        var page = ruleService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FertilizationRule>()
                        .orderByDesc(FertilizationRule::getPriority)
        );
        return ResultVO.success(PageVO.of(page.getTotal(), dto.getPage(), dto.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<FertilizationRule> getById(@PathVariable Long id) {
        return ResultVO.success(ruleService.getById(id));
    }

    @PostMapping
    public ResultVO<?> save(@Valid @RequestBody FertilizationRuleDTO dto) {
        var rule = new FertilizationRule();
        BeanUtils.copyProperties(dto, rule);
        ruleService.save(rule);
        return ResultVO.success();
    }

    @PutMapping
    public ResultVO<?> update(@RequestBody FertilizationRule rule) {
        ruleService.updateById(rule);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        ruleService.removeById(id);
        return ResultVO.success();
    }

    @PostMapping("/recommend")
    public ResultVO<?> recommend(@Valid @RequestBody RecommendRequestDTO dto) {
        var result = ruleService.recommend(dto.getSpecies(), dto.getAge(), dto.getSeason());
        return ResultVO.success(result);
    }
}
