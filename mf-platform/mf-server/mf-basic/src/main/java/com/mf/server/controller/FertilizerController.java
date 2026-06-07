package com.mf.server.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.Fertilizer;
import com.mf.server.service.FertilizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/fertilizer") @RequiredArgsConstructor
public class FertilizerController {
    private final FertilizerService service;

    @GetMapping public ResultVO<PageVO<Fertilizer>> list(@ModelAttribute PageDTO p, @RequestParam(required=false) String name) {
        var w = new LambdaQueryWrapper<Fertilizer>().like(name!=null,Fertilizer::getName,name).orderByDesc(Fertilizer::getCreateTime);
        var pg = service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @GetMapping("/{id}") public ResultVO<Fertilizer> detail(@PathVariable Long id) { return ResultVO.success(service.getById(id)); }
    @PostMapping public ResultVO<?> save(@RequestBody Fertilizer f) { service.save(f); return ResultVO.success(); }
    @PutMapping public ResultVO<?> update(@RequestBody Fertilizer f) { service.updateById(f); return ResultVO.success(); }
    @DeleteMapping("/{id}") public ResultVO<?> delete(@PathVariable Long id) { service.removeById(id); return ResultVO.success(); }
    @GetMapping("/list") public ResultVO<?> cachedList() { return ResultVO.success(service.lambdaQuery().list()); }
}
