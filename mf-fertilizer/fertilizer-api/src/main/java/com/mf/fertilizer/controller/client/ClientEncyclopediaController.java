package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.EncyclopediaEntry;
import com.mf.fertilizer.service.EncyclopediaEntryService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/encyclopedia")
@RequiredArgsConstructor
public class ClientEncyclopediaController {
    private final EncyclopediaEntryService service;

    @GetMapping
    public ResultVO<PageVO<EncyclopediaEntry>> list(@ModelAttribute PageDTO page,
                                                     @RequestParam(name = "keyword", required = false) String keyword,
                                                     @RequestParam(name = "categoryId", required = false) Long categoryId) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EncyclopediaEntry>()
                .eq(EncyclopediaEntry::getIsPublished, 1)
                .like(keyword != null, EncyclopediaEntry::getName, keyword)
                .eq(categoryId != null, EncyclopediaEntry::getCategoryId, categoryId)
                .orderByDesc(EncyclopediaEntry::getViewCount);
        var p = service.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<EncyclopediaEntry> detail(@PathVariable Long id) {
        var e = service.getById(id);
        if (e == null) return ResultVO.fail(404, "词条不存在");
        e.setViewCount(e.getViewCount() + 1);
        service.updateById(e);
        return ResultVO.success(e);
    }
}
