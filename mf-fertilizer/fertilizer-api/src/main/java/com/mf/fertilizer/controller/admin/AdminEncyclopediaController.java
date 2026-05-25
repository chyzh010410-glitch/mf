package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.EncyclopediaSaveDTO;
import com.mf.fertilizer.entity.EncyclopediaEntry;
import com.mf.fertilizer.service.EncyclopediaEntryService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/encyclopedia")
@RequiredArgsConstructor
public class AdminEncyclopediaController {

    private final EncyclopediaEntryService entryService;

    @GetMapping
    public ResultVO<PageVO<EncyclopediaEntry>> list(@ModelAttribute PageDTO page,
                                                     @RequestParam(required = false) String keyword,
                                                     @RequestParam(required = false) Integer isPublished) {
        var w = new LambdaQueryWrapper<EncyclopediaEntry>()
                .and(StrUtil.isNotBlank(keyword), q -> q
                        .like(EncyclopediaEntry::getName, keyword)
                        .or().like(EncyclopediaEntry::getScientificName, keyword)
                        .or().like(EncyclopediaEntry::getAlias, keyword))
                .eq(isPublished != null, EncyclopediaEntry::getIsPublished, isPublished)
                .orderByDesc(EncyclopediaEntry::getCreateTime);
        var p = entryService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<EncyclopediaEntry> detail(@PathVariable Long id) {
        var entry = entryService.getById(id);
        if (entry == null) return ResultVO.fail(404, "百科词条不存在");
        return ResultVO.success(entry);
    }

    @PostMapping
    @OperationLog(module = "百科管理", action = "新增")
    public ResultVO<?> save(@RequestBody EncyclopediaSaveDTO dto) {
        var entry = new EncyclopediaEntry();
        BeanUtils.copyProperties(dto, entry);
        if (entry.getIsPublished() == null) entry.setIsPublished(0);
        if (entry.getViewCount() == null) entry.setViewCount(0);
        entryService.save(entry);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "百科管理", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody EncyclopediaSaveDTO dto) {
        var entry = entryService.getById(id);
        if (entry == null) return ResultVO.fail(404, "百科词条不存在");
        BeanUtils.copyProperties(dto, entry);
        entry.setId(id);
        entryService.updateById(entry);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "百科管理", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        entryService.removeById(id);
        return ResultVO.success();
    }

    @PutMapping("/{id}/publish")
    @OperationLog(module = "百科管理", action = "发布/下架")
    public ResultVO<?> togglePublish(@PathVariable Long id) {
        var entry = entryService.getById(id);
        if (entry == null) return ResultVO.fail(404, "百科词条不存在");
        entry.setIsPublished(entry.getIsPublished() == 1 ? 0 : 1);
        entryService.updateById(entry);
        return ResultVO.success();
    }
}
