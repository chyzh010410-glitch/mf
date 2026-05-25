package com.mf.fertilizer.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.ProductCategory;
import com.mf.fertilizer.service.ProductCategoryService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final ProductCategoryService categoryService;

    @GetMapping
    public ResultVO<PageVO<ProductCategory>> list(@ModelAttribute PageDTO page,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String type) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductCategory>()
                .like(name != null, ProductCategory::getName, name)
                .eq(type != null, ProductCategory::getType, type)
                .orderByAsc(ProductCategory::getSortOrder)
                .orderByDesc(ProductCategory::getCreateTime);
        var p = categoryService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<ProductCategory> detail(@PathVariable Long id) {
        var cat = categoryService.getById(id);
        if (cat == null) return ResultVO.fail(404, "分类不存在");
        return ResultVO.success(cat);
    }

    @PostMapping
    @OperationLog(module = "分类管理", action = "新增")
    public ResultVO<?> save(@RequestBody ProductCategory dto) {
        if (dto.getSortOrder() == null) dto.setSortOrder(0);
        categoryService.save(dto);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "分类管理", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody ProductCategory dto) {
        var cat = categoryService.getById(id);
        if (cat == null) return ResultVO.fail(404, "分类不存在");
        BeanUtils.copyProperties(dto, cat);
        cat.setId(id);
        categoryService.updateById(cat);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "分类管理", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return ResultVO.success();
    }
}
