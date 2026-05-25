package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.ArticleSaveDTO;
import com.mf.fertilizer.entity.EncyclopediaArticle;
import com.mf.fertilizer.service.EncyclopediaArticleService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/articles")
@RequiredArgsConstructor
public class AdminArticleController {

    private final EncyclopediaArticleService articleService;

    @GetMapping
    public ResultVO<PageVO<EncyclopediaArticle>> list(@ModelAttribute PageDTO page,
                                                       @RequestParam(required = false) String keyword,
                                                       @RequestParam(required = false) Integer isPublished,
                                                       @RequestParam(required = false) Integer isTop,
                                                       @RequestParam(required = false) Integer isRecommend) {
        var w = new LambdaQueryWrapper<EncyclopediaArticle>()
                .like(StrUtil.isNotBlank(keyword), EncyclopediaArticle::getTitle, keyword)
                .eq(isPublished != null, EncyclopediaArticle::getIsPublished, isPublished)
                .eq(isTop != null, EncyclopediaArticle::getIsTop, isTop)
                .eq(isRecommend != null, EncyclopediaArticle::getIsRecommend, isRecommend)
                .orderByDesc(EncyclopediaArticle::getIsTop)
                .orderByDesc(EncyclopediaArticle::getCreateTime);
        var p = articleService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<EncyclopediaArticle> detail(@PathVariable Long id) {
        var article = articleService.getById(id);
        if (article == null) return ResultVO.fail(404, "文章不存在");
        return ResultVO.success(article);
    }

    @PostMapping
    @OperationLog(module = "文章管理", action = "新增")
    public ResultVO<?> save(@RequestBody ArticleSaveDTO dto) {
        var article = new EncyclopediaArticle();
        BeanUtils.copyProperties(dto, article);
        if (article.getIsPublished() == null) article.setIsPublished(0);
        if (article.getIsTop() == null) article.setIsTop(0);
        if (article.getIsRecommend() == null) article.setIsRecommend(0);
        if (article.getViewCount() == null) article.setViewCount(0);
        articleService.save(article);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "文章管理", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody ArticleSaveDTO dto) {
        var article = articleService.getById(id);
        if (article == null) return ResultVO.fail(404, "文章不存在");
        BeanUtils.copyProperties(dto, article);
        article.setId(id);
        articleService.updateById(article);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "文章管理", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        articleService.removeById(id);
        return ResultVO.success();
    }

    @PutMapping("/{id}/publish")
    @OperationLog(module = "文章管理", action = "发布/下架")
    public ResultVO<?> togglePublish(@PathVariable Long id) {
        var article = articleService.getById(id);
        if (article == null) return ResultVO.fail(404, "文章不存在");
        article.setIsPublished(article.getIsPublished() == 1 ? 0 : 1);
        articleService.updateById(article);
        return ResultVO.success();
    }

    @PutMapping("/{id}/top")
    public ResultVO<?> toggleTop(@PathVariable Long id) {
        var article = articleService.getById(id);
        if (article == null) return ResultVO.fail(404, "文章不存在");
        article.setIsTop(article.getIsTop() == 1 ? 0 : 1);
        articleService.updateById(article);
        return ResultVO.success();
    }

    @PutMapping("/{id}/recommend")
    public ResultVO<?> toggleRecommend(@PathVariable Long id) {
        var article = articleService.getById(id);
        if (article == null) return ResultVO.fail(404, "文章不存在");
        article.setIsRecommend(article.getIsRecommend() == 1 ? 0 : 1);
        articleService.updateById(article);
        return ResultVO.success();
    }
}
