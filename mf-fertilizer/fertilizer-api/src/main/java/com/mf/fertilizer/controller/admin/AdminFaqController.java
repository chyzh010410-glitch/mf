package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.FaqSaveDTO;
import com.mf.fertilizer.entity.Faq;
import com.mf.fertilizer.service.FaqService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/faqs")
@RequiredArgsConstructor
public class AdminFaqController {

    private final FaqService faqService;

    @GetMapping
    public ResultVO<PageVO<Faq>> list(@ModelAttribute PageDTO page,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String category) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Faq>()
                .like(StrUtil.isNotBlank(keyword), Faq::getQuestion, keyword)
                .eq(StrUtil.isNotBlank(category), Faq::getCategory, category)
                .orderByAsc(Faq::getSortOrder)
                .orderByDesc(Faq::getCreateTime);
        var p = faqService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<Faq> detail(@PathVariable Long id) {
        var faq = faqService.getById(id);
        if (faq == null) return ResultVO.fail(404, "FAQ不存在");
        return ResultVO.success(faq);
    }

    @PostMapping
    @OperationLog(module = "FAQ管理", action = "新增")
    public ResultVO<?> save(@RequestBody FaqSaveDTO dto) {
        var faq = new Faq();
        BeanUtils.copyProperties(dto, faq);
        if (faq.getIsPublished() == null) faq.setIsPublished(0);
        if (faq.getSortOrder() == null) faq.setSortOrder(0);
        faqService.save(faq);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "FAQ管理", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody FaqSaveDTO dto) {
        var faq = faqService.getById(id);
        if (faq == null) return ResultVO.fail(404, "FAQ不存在");
        BeanUtils.copyProperties(dto, faq);
        faq.setId(id);
        faqService.updateById(faq);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "FAQ管理", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        faqService.removeById(id);
        return ResultVO.success();
    }
}
