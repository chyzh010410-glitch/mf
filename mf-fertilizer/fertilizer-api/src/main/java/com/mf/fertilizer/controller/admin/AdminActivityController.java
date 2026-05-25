package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.ActivitySaveDTO;
import com.mf.fertilizer.entity.ActivityEntity;
import com.mf.fertilizer.service.ActivityEntityService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/activities")
@RequiredArgsConstructor
public class AdminActivityController {

    private final ActivityEntityService activityService;

    @GetMapping
    public ResultVO<PageVO<ActivityEntity>> list(@ModelAttribute PageDTO page,
                                                   @RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) String status,
                                                   @RequestParam(required = false) String type) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ActivityEntity>()
                .like(StrUtil.isNotBlank(keyword), ActivityEntity::getTitle, keyword)
                .eq(StrUtil.isNotBlank(status), ActivityEntity::getStatus, status)
                .eq(StrUtil.isNotBlank(type), ActivityEntity::getType, type)
                .orderByDesc(ActivityEntity::getSortOrder)
                .orderByDesc(ActivityEntity::getCreateTime);
        var p = activityService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<ActivityEntity> detail(@PathVariable Long id) {
        var a = activityService.getById(id);
        if (a == null) return ResultVO.fail(404, "活动不存在");
        return ResultVO.success(a);
    }

    @PostMapping
    @OperationLog(module = "活动管理", action = "新增")
    public ResultVO<?> save(@RequestBody ActivitySaveDTO dto) {
        var a = new ActivityEntity();
        BeanUtils.copyProperties(dto, a);
        if (a.getSortOrder() == null) a.setSortOrder(0);
        if (a.getIsBanner() == null) a.setIsBanner(0);
        a.setStatus("active");
        activityService.save(a);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "活动管理", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody ActivitySaveDTO dto) {
        var a = activityService.getById(id);
        if (a == null) return ResultVO.fail(404, "活动不存在");
        BeanUtils.copyProperties(dto, a);
        a.setId(id);
        activityService.updateById(a);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "活动管理", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        activityService.removeById(id);
        return ResultVO.success();
    }

    @PutMapping("/{id}/status")
    @OperationLog(module = "活动管理", action = "状态变更")
    public ResultVO<?> toggleStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        var a = activityService.getById(id);
        if (a == null) return ResultVO.fail(404, "活动不存在");
        a.setStatus(body.get("status"));
        activityService.updateById(a);
        return ResultVO.success();
    }
}
