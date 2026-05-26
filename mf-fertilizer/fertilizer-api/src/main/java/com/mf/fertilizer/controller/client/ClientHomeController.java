package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.entity.*;
import com.mf.fertilizer.service.ActivityEntityService;
import com.mf.fertilizer.service.EncyclopediaArticleService;
import com.mf.fertilizer.service.ProductService;
import com.mf.fertilizer.vo.ResultVO;
import com.mf.fertilizer.vo.client.HomePageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/client/home")
@RequiredArgsConstructor
public class ClientHomeController {
    private final ActivityEntityService activityService;
    private final ProductService productService;
    private final EncyclopediaArticleService articleService;

    @GetMapping
    public ResultVO<HomePageVO> index() {
        var vo = new HomePageVO();
        var activities = activityService.lambdaQuery().eq(ActivityEntity::getStatus, "active").eq(ActivityEntity::getIsBanner, 1).orderByAsc(ActivityEntity::getSortOrder).list();
        var banners = new ArrayList<HomePageVO.BannerItem>();
        for (var a : activities) banners.add(new HomePageVO.BannerItem(a.getId(), a.getCoverImage(), a.getTitle(), "activity"));
        vo.setBanners(banners);
        var products = productService.lambdaQuery().eq(Product::getStatus, 1).gt(Product::getStock, 0).eq(Product::getIsRecommend, 1).orderByDesc(Product::getSalesCount).last("limit 8").list();
        var pcs = new ArrayList<HomePageVO.ProductCard>();
        for (var p : products) pcs.add(new HomePageVO.ProductCard(p.getId(), p.getName(), p.getCoverImage(), p.getPrice(), p.getSalesCount()));
        vo.setRecommendedProducts(pcs);
        var newProducts = productService.lambdaQuery().eq(Product::getStatus, 1).gt(Product::getStock, 0).orderByDesc(Product::getCreateTime).last("limit 8").list();
        var nps = new ArrayList<HomePageVO.ProductCard>();
        for (var p : newProducts) nps.add(new HomePageVO.ProductCard(p.getId(), p.getName(), p.getCoverImage(), p.getPrice(), p.getSalesCount()));
        vo.setNewProducts(nps);
        var articles = articleService.lambdaQuery().eq(EncyclopediaArticle::getIsPublished, 1).eq(EncyclopediaArticle::getIsRecommend, 1).orderByDesc(EncyclopediaArticle::getCreateTime).last("limit 4").list();
        var acs = new ArrayList<HomePageVO.ArticleCard>();
        for (var a : articles) acs.add(new HomePageVO.ArticleCard(a.getId(), a.getTitle(), a.getCoverImage(), a.getSummary()));
        vo.setRecommendedArticles(acs);
        return ResultVO.success(vo);
    }
}
