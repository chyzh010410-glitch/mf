package com.mf.fertilizer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.FertilizerQueryDTO;
import com.mf.fertilizer.entity.Fertilizer;
import com.mf.fertilizer.service.FertilizerService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fertilizer")
@RequiredArgsConstructor
public class FertilizerController {

    private final FertilizerService fertilizerService;

    @GetMapping("/page")
    public ResultVO<PageVO<Fertilizer>> page(@Valid FertilizerQueryDTO dto) {
        var page = fertilizerService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<Fertilizer>()
                        .like(StrUtil.isNotBlank(dto.getName()), Fertilizer::getName, dto.getName())
                        .eq(StrUtil.isNotBlank(dto.getType()), Fertilizer::getType, dto.getType())
                        .orderByDesc(Fertilizer::getCreateTime)
        );
        return ResultVO.success(PageVO.of(page.getTotal(), dto.getPage(), dto.getSize(), page.getRecords()));
    }

    @GetMapping("/list")
    public ResultVO<?> list() {
        return ResultVO.success(fertilizerService.getCachedList());
    }

    @GetMapping("/{id}")
    public ResultVO<Fertilizer> getById(@PathVariable Long id) {
        return ResultVO.success(fertilizerService.getById(id));
    }

    @PostMapping
    public ResultVO<?> save(@RequestBody Fertilizer fertilizer) {
        fertilizerService.save(fertilizer);
        fertilizerService.evictCache();
        return ResultVO.success();
    }

    @PutMapping
    public ResultVO<?> update(@RequestBody Fertilizer fertilizer) {
        fertilizerService.updateById(fertilizer);
        fertilizerService.evictCache();
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        fertilizerService.removeById(id);
        fertilizerService.evictCache();
        return ResultVO.success();
    }
}
