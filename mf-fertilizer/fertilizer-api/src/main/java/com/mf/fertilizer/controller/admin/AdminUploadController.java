package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.UploadReviewDTO;
import com.mf.fertilizer.entity.UserUpload;
import com.mf.fertilizer.service.UserUploadService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/admin/uploads")
@RequiredArgsConstructor
public class AdminUploadController {

    private final UserUploadService uploadService;

    @GetMapping
    public ResultVO<PageVO<UserUpload>> list(@ModelAttribute PageDTO page,
                                              @RequestParam(required = false) String status,
                                              @RequestParam(required = false) String keyword) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserUpload>()
                .eq(StrUtil.isNotBlank(status), UserUpload::getStatus, status)
                .like(StrUtil.isNotBlank(keyword), UserUpload::getName, keyword)
                .orderByDesc(UserUpload::getCreateTime);
        var p = uploadService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PutMapping("/{id}/review")
    @OperationLog(module = "上传审核", action = "审核")
    public ResultVO<?> review(@PathVariable Long id, @RequestBody UploadReviewDTO dto) {
        var upload = uploadService.getById(id);
        if (upload == null) return ResultVO.fail(404, "上传记录不存在");
        upload.setStatus(dto.getStatus());
        upload.setReviewComment(dto.getReviewComment());
        upload.setReviewerId(com.mf.fertilizer.context.UserContext.getUserId());
        upload.setReviewTime(LocalDateTime.now());
        uploadService.updateById(upload);
        return ResultVO.success();
    }
}
