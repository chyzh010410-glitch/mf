package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.EncyclopediaArticle;
import com.mf.server.service.EncyclopediaArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/articles") @RequiredArgsConstructor
public class ArticleController {
    private final EncyclopediaArticleService service;
    @GetMapping public ResultVO<PageVO<EncyclopediaArticle>> list(@ModelAttribute PageDTO p) {
        var w=new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EncyclopediaArticle>()
                .eq(EncyclopediaArticle::getIsPublished,1).orderByDesc(EncyclopediaArticle::getIsTop).orderByDesc(EncyclopediaArticle::getCreateTime);
        var pg=service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @GetMapping("/{id}") public ResultVO<EncyclopediaArticle> detail(@PathVariable Long id) {
        var a=service.getById(id); if(a!=null) { a.setViewCount(a.getViewCount()+1); service.updateById(a); }
        return ResultVO.success(a);
    }
}