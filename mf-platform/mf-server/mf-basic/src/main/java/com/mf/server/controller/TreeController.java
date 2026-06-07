package com.mf.server.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.Tree;
import com.mf.server.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/tree") @RequiredArgsConstructor
public class TreeController {
    private final TreeService service;

    @GetMapping public ResultVO<PageVO<Tree>> list(@ModelAttribute PageDTO p, @RequestParam(required=false) String species) {
        var w = new LambdaQueryWrapper<Tree>().like(species!=null,Tree::getSpecies,species).orderByDesc(Tree::getCreateTime);
        var pg = service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @GetMapping("/{id}") public ResultVO<Tree> detail(@PathVariable Long id) { return ResultVO.success(service.getById(id)); }
    @PostMapping public ResultVO<?> save(@RequestBody Tree t) { service.save(t); return ResultVO.success(); }
    @PutMapping public ResultVO<?> update(@RequestBody Tree t) { service.updateById(t); return ResultVO.success(); }
    @DeleteMapping("/{id}") public ResultVO<?> delete(@PathVariable Long id) { service.removeById(id); return ResultVO.success(); }
    @GetMapping("/species") public ResultVO<?> species() {
        var list = service.lambdaQuery().select(Tree::getSpecies).groupBy(Tree::getSpecies).list().stream().map(Tree::getSpecies).distinct().toList();
        return ResultVO.success(list);
    }
}
