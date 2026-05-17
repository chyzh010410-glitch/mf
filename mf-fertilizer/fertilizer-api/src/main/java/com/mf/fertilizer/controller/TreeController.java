package com.mf.fertilizer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.TreeQueryDTO;
import com.mf.fertilizer.entity.Tree;
import com.mf.fertilizer.service.TreeService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tree")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService treeService;

    @GetMapping("/page")
    public ResultVO<PageVO<Tree>> page(@Valid TreeQueryDTO dto) {
        var page = treeService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<Tree>()
                        .like(StrUtil.isNotBlank(dto.getSpecies()), Tree::getSpecies, dto.getSpecies())
                        .eq(StrUtil.isNotBlank(dto.getStatus()), Tree::getStatus, dto.getStatus())
                        .ge(dto.getAgeMin() != null, Tree::getAge, dto.getAgeMin())
                        .le(dto.getAgeMax() != null, Tree::getAge, dto.getAgeMax())
                        .orderByDesc(Tree::getCreateTime)
        );
        return ResultVO.success(PageVO.of(page.getTotal(), dto.getPage(), dto.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<Tree> getById(@PathVariable Long id) {
        return ResultVO.success(treeService.getById(id));
    }

    @PostMapping
    public ResultVO<?> save(@RequestBody Tree tree) {
        treeService.save(tree);
        treeService.evictCache();
        return ResultVO.success();
    }

    @PutMapping
    public ResultVO<?> update(@RequestBody Tree tree) {
        treeService.updateById(tree);
        treeService.evictCache();
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        treeService.removeById(id);
        treeService.evictCache();
        return ResultVO.success();
    }

    @GetMapping("/species")
    public ResultVO<?> species() {
        return ResultVO.success(treeService.getCachedSpecies());
    }
}
