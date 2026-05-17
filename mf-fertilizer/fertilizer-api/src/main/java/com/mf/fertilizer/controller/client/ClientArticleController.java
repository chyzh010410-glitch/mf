package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.EncyclopediaArticle;
import com.mf.fertilizer.service.EncyclopediaArticleService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/articles")
@RequiredArgsConstructor
public class ClientArticleController {
    private final EncyclopediaArticleService service;

    @GetMapping
    public ResultVO<PageVO<EncyclopediaArticle>> list(@ModelAttribute PageDTO page,
                                                       @RequestParam(name = "categoryId", required = false) Long categoryId,
                                                       @RequestParam(name = "isRecommend", required = false) Integer isRecommend) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EncyclopediaArticle>()
                .eq(EncyclopediaArticle::getIsPublished, 1)
                .eq(categoryId != null, EncyclopediaArticle::getCategoryId, categoryId)
                .eq(isRecommend != null, EncyclopediaArticle::getIsRecommend, isRecommend)
                .orderByDesc(EncyclopediaArticle::getCreateTime);
        var p = service.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<EncyclopediaArticle> detail(@PathVariable Long id) {
        var a = service.getById(id);
        if (a == null) return ResultVO.fail(404, "文章不存在");
        a.setViewCount(a.getViewCount() + 1);
        service.updateById(a);
        return ResultVO.success(a);
    }
}
