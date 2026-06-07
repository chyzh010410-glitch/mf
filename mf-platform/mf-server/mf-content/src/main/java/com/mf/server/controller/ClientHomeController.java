package com.mf.server.controller;
import com.mf.client.ProductClient;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.ActivityEntity;
import com.mf.server.entity.EncyclopediaArticle;
import com.mf.server.service.ActivityEntityService;
import com.mf.server.service.EncyclopediaArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/client/home") @RequiredArgsConstructor
public class ClientHomeController {
    private final ActivityEntityService activityService;
    private final EncyclopediaArticleService articleService;
    private final ProductClient productClient;

    @SuppressWarnings("unchecked")
    @GetMapping
    public ResultVO<?> index() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("banners", activityService.lambdaQuery()
                .eq(ActivityEntity::getStatus, "active")
                .eq(ActivityEntity::getIsBanner, 1)
                .orderByAsc(ActivityEntity::getSortOrder).list());

        // Feign 调用商品服务获取推荐商品
        try {
            var resp = productClient.listProducts(null, null);
            var records = (List<Map<String, Object>>) ((Map<String, Object>)resp.get("data")).get("records");
            data.put("recommendedProducts", records != null ? records : List.of());
            data.put("newProducts", records != null ? records : List.of());
        } catch (Exception e) {
            data.put("recommendedProducts", List.of());
            data.put("newProducts", List.of());
        }

        data.put("recommendedArticles", articleService.lambdaQuery()
                .eq(EncyclopediaArticle::getIsPublished, 1)
                .eq(EncyclopediaArticle::getIsRecommend, 1)
                .orderByDesc(EncyclopediaArticle::getCreateTime)
                .last("limit 4").list());
        return ResultVO.success(data);
    }
}
