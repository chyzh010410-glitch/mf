package com.mf.server.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.Product;
import com.mf.server.entity.ProductCategory;
import com.mf.server.entity.ProductDetail;
import com.mf.server.search.ProductSearchService;
import com.mf.server.service.ProductCategoryService;
import com.mf.server.service.ProductDetailService;
import com.mf.server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/client/products") @RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductDetailService detailService;
    private final ProductCategoryService categoryService;
    private final ProductSearchService searchService;

    @GetMapping
    public ResultVO<PageVO<Product>> list(@ModelAttribute PageDTO p,
            @RequestParam(required=false) Long categoryId, @RequestParam(required=false) String keyword,
            @RequestParam(required=false) String sort, @RequestParam(required=false) String productType) {
        var w = new LambdaQueryWrapper<Product>().eq(Product::getStatus,1).gt(Product::getStock,0)
                .eq(categoryId!=null,Product::getCategoryId,categoryId)
                .eq(productType!=null,Product::getProductType,productType);
        // 有关键词走 ES 分词搜索，无关键词走 MySQL
        if (keyword != null && !keyword.isBlank()) {
            try {
                var ids = searchService.search(keyword);
                if (ids.isEmpty()) return ResultVO.success(PageVO.of(0L,p.getPage(),p.getSize(),List.of()));
                w.in(Product::getId, ids);
            } catch (Exception e) { w.like(Product::getName, keyword); }
        }
        if("sales".equals(sort)) w.orderByDesc(Product::getSalesCount);
        else if("price_asc".equals(sort)) w.orderByAsc(Product::getPrice);
        else if("price_desc".equals(sort)) w.orderByDesc(Product::getPrice);
        else w.orderByDesc(Product::getCreateTime);
        var pg=productService.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }

    /** 重建 ES 索引 */
    @PostMapping("/rebuild-index")
    public ResultVO<?> rebuildIndex() {
        try {
            var all = productService.lambdaQuery().eq(Product::getStatus,1).list();
            searchService.rebuild(all);
            return ResultVO.success(Map.of("indexed", all.size()));
        } catch (Exception e) {
            return ResultVO.fail(503, "ES 不可用: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResultVO<com.mf.common.vo.ProductVO> detail(@PathVariable Long id) {
        var product=productService.getById(id);
        if(product==null||product.getStatus()==0) return ResultVO.fail(404,"商品不存在");
        var detail=detailService.lambdaQuery().eq(ProductDetail::getProductId,id).one();
        var vo = new com.mf.common.vo.ProductVO();
        vo.setId(product.getId()); vo.setName(product.getName()); vo.setProductType(product.getProductType());
        vo.setCategoryId(product.getCategoryId()); vo.setBrand(product.getBrand());
        vo.setCoverImage(product.getCoverImage()); vo.setImages(product.getImages());
        vo.setPrice(product.getPrice()); vo.setOriginalPrice(product.getOriginalPrice());
        vo.setStock(product.getStock()); vo.setUnit(product.getUnit()); vo.setSalesCount(product.getSalesCount());
        vo.setStatus(product.getStatus()); vo.setIsRecommend(product.getIsRecommend()); vo.setIsNew(product.getIsNew());
        vo.setDescription(product.getDescription()); vo.setFreight(product.getFreight());
        if(detail!=null){ vo.setDetailType(detail.getDetailType()); vo.setAttrsJson(detail.getAttrsJson()); }
        return ResultVO.success(vo);
    }

    @GetMapping("/categories")
    public ResultVO<?> categories(@RequestParam(required=false) String type) {
        var list=categoryService.lambdaQuery()
                .eq(type!=null,ProductCategory::getType,type)
                .eq(ProductCategory::getParentId,0L).orderByAsc(ProductCategory::getSortOrder).list();
        return ResultVO.success(list);
    }
}
