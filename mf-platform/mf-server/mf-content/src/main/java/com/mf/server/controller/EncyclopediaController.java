package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.EncyclopediaEntry;
import com.mf.server.service.EncyclopediaEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/encyclopedia") @RequiredArgsConstructor
public class EncyclopediaController {
    private final EncyclopediaEntryService service;
    @GetMapping public ResultVO<PageVO<EncyclopediaEntry>> list(@ModelAttribute PageDTO p, @RequestParam(required=false) String keyword) {
        var w=new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EncyclopediaEntry>()
                .eq(EncyclopediaEntry::getIsPublished,1).like(keyword!=null,EncyclopediaEntry::getName,keyword)
                .orderByDesc(EncyclopediaEntry::getViewCount);
        var pg=service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @GetMapping("/{id}") public ResultVO<EncyclopediaEntry> detail(@PathVariable Long id) {
        var e=service.getById(id); if(e!=null) { e.setViewCount(e.getViewCount()+1); service.updateById(e); }
        return ResultVO.success(e);
    }
}