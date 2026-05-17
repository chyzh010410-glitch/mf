package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.Product;
import com.mf.fertilizer.entity.ProductDetail;
import com.mf.fertilizer.service.ProductCategoryService;
import com.mf.fertilizer.service.ProductDetailService;
import com.mf.fertilizer.service.ProductService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import com.mf.fertilizer.vo.client.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/products")
@RequiredArgsConstructor
public class ClientProductController {

    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ProductCategoryService categoryService;

    @GetMapping
    public ResultVO<PageVO<ProductVO>> list(@ModelAttribute PageDTO page,
                                            @RequestParam(name = "categoryId", required = false) Long categoryId,
                                            @RequestParam(name = "keyword", required = false) String keyword,
                                            @RequestParam(name = "sort", required = false) String sort,
                                            @RequestParam(name = "productType", required = false) String productType,
                                            @RequestParam(name = "minPrice", required = false) java.math.BigDecimal minPrice,
                                            @RequestParam(name = "maxPrice", required = false) java.math.BigDecimal maxPrice) {
        var wrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .eq(productType != null, Product::getProductType, productType)
                .like(keyword != null, Product::getName, keyword)
                .ge(minPrice != null, Product::getPrice, minPrice)
                .le(maxPrice != null, Product::getPrice, maxPrice);
        if ("sales".equals(sort)) wrapper.orderByDesc(Product::getSalesCount);
        else if ("price_asc".equals(sort)) wrapper.orderByAsc(Product::getPrice);
        else if ("price_desc".equals(sort)) wrapper.orderByDesc(Product::getPrice);
        else wrapper.orderByDesc(Product::getCreateTime);

        var p = productService.page(new Page<>(page.getPage(), page.getSize()), wrapper);
        var records = p.getRecords().stream().map(this::toVO).toList();
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), records));
    }

    @GetMapping("/{id}")
    public ResultVO<ProductVO> detail(@PathVariable Long id) {
        var product = productService.getById(id);
        if (product == null || product.getStatus() == 0) return ResultVO.fail(404, "商品不存在");
        var detail = productDetailService.lambdaQuery().eq(ProductDetail::getProductId, id).one();
        var vo = toVO(product);
        if (detail != null) {
            vo.setDetailType(detail.getDetailType());
            vo.setAttrsJson(detail.getAttrsJson());
        }
        return ResultVO.success(vo);
    }

    @GetMapping("/categories")
    public ResultVO<?> categories(@RequestParam(required = false) String type) {
        var list = categoryService.lambdaQuery()
                .eq(type != null, com.mf.fertilizer.entity.ProductCategory::getType, type)
                .eq(com.mf.fertilizer.entity.ProductCategory::getParentId, 0L)
                .orderByAsc(com.mf.fertilizer.entity.ProductCategory::getSortOrder)
                .list();
        return ResultVO.success(list);
    }

    private ProductVO toVO(Product p) {
        var vo = new ProductVO();
        vo.setId(p.getId()); vo.setName(p.getName()); vo.setProductType(p.getProductType());
        vo.setBrand(p.getBrand()); vo.setCoverImage(p.getCoverImage());
        vo.setPrice(p.getPrice()); vo.setOriginalPrice(p.getOriginalPrice());
        vo.setStock(p.getStock()); vo.setUnit(p.getUnit()); vo.setSalesCount(p.getSalesCount());
        vo.setStatus(p.getStatus()); vo.setIsRecommend(p.getIsRecommend()); vo.setIsNew(p.getIsNew());
        vo.setDescription(p.getDescription()); vo.setFreight(p.getFreight());
        return vo;
    }
}
