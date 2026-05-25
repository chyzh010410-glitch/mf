package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.ProductSaveDTO;
import com.mf.fertilizer.entity.Product;
import com.mf.fertilizer.entity.ProductDetail;
import com.mf.fertilizer.service.ProductCategoryService;
import com.mf.fertilizer.service.ProductDetailService;
import com.mf.fertilizer.service.ProductService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final ProductDetailService detailService;
    private final ProductCategoryService categoryService;

    @GetMapping
    public ResultVO<PageVO<Product>> list(@ModelAttribute PageDTO page,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String productType,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Integer status) {
        var w = new LambdaQueryWrapper<Product>()
                .like(StrUtil.isNotBlank(name), Product::getName, name)
                .eq(StrUtil.isNotBlank(productType), Product::getProductType, productType)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .eq(status != null, Product::getStatus, status)
                .orderByDesc(Product::getSortOrder)
                .orderByDesc(Product::getCreateTime);
        var p = productService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<?> detail(@PathVariable Long id) {
        var product = productService.getById(id);
        if (product == null) return ResultVO.fail(404, "商品不存在");
        var detail = detailService.lambdaQuery().eq(ProductDetail::getProductId, id).one();
        String categoryName = null;
        if (product.getCategoryId() != null) {
            var cat = categoryService.getById(product.getCategoryId());
            if (cat != null) categoryName = cat.getName();
        }
        return ResultVO.success(Map.of("product", product,
                "detail", detail != null ? detail : new ProductDetail(),
                "categoryName", categoryName != null ? categoryName : ""));
    }

    @PostMapping
    @OperationLog(module = "商品管理", action = "新增")
    public ResultVO<?> save(@RequestBody ProductSaveDTO dto) {
        var product = new Product();
        BeanUtils.copyProperties(dto, product);
        if (product.getStatus() == null) product.setStatus(1);
        if (product.getStock() == null) product.setStock(0);
        if (product.getSalesCount() == null) product.setSalesCount(0);
        if (product.getIsRecommend() == null) product.setIsRecommend(0);
        if (product.getIsNew() == null) product.setIsNew(0);
        productService.save(product);

        if (StrUtil.isNotBlank(dto.getAttrsJson()) || StrUtil.isNotBlank(dto.getDetailType())) {
            var detail = new ProductDetail();
            detail.setProductId(product.getId());
            detail.setDetailType(dto.getDetailType());
            detail.setAttrsJson(dto.getAttrsJson());
            detailService.save(detail);
        }
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "商品管理", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody ProductSaveDTO dto) {
        var product = productService.getById(id);
        if (product == null) return ResultVO.fail(404, "商品不存在");
        BeanUtils.copyProperties(dto, product);
        product.setId(id);
        productService.updateById(product);

        if (StrUtil.isNotBlank(dto.getAttrsJson()) || StrUtil.isNotBlank(dto.getDetailType())) {
            var detail = detailService.lambdaQuery().eq(ProductDetail::getProductId, id).one();
            if (detail == null) {
                detail = new ProductDetail();
                detail.setProductId(id);
            }
            detail.setDetailType(dto.getDetailType());
            detail.setAttrsJson(dto.getAttrsJson());
            detailService.saveOrUpdate(detail);
        }
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "商品管理", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        productService.removeById(id);
        detailService.lambdaUpdate().eq(ProductDetail::getProductId, id).remove();
        return ResultVO.success();
    }

    @PutMapping("/{id}/status")
    @OperationLog(module = "商品管理", action = "上下架")
    public ResultVO<?> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        var product = productService.getById(id);
        if (product == null) return ResultVO.fail(404, "商品不存在");
        product.setStatus(body.get("status"));
        productService.updateById(product);
        return ResultVO.success();
    }

    @PutMapping("/{id}/recommend")
    public ResultVO<?> toggleRecommend(@PathVariable Long id) {
        var product = productService.getById(id);
        if (product == null) return ResultVO.fail(404, "商品不存在");
        product.setIsRecommend(product.getIsRecommend() == 1 ? 0 : 1);
        productService.updateById(product);
        return ResultVO.success();
    }

    @PutMapping("/{id}/new")
    public ResultVO<?> toggleNew(@PathVariable Long id) {
        var product = productService.getById(id);
        if (product == null) return ResultVO.fail(404, "商品不存在");
        product.setIsNew(product.getIsNew() == 1 ? 0 : 1);
        productService.updateById(product);
        return ResultVO.success();
    }
}
