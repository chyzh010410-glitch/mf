package com.mf.fertilizer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.FertilizationRecordDTO;
import com.mf.fertilizer.dto.RecordQueryDTO;
import com.mf.fertilizer.entity.FertilizationRecord;
import com.mf.fertilizer.service.FertilizationRecordService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class FertilizationRecordController {

    private final FertilizationRecordService recordService;

    @GetMapping("/page")
    public ResultVO<PageVO<FertilizationRecord>> page(@Valid RecordQueryDTO dto) {
        var page = recordService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<FertilizationRecord>()
                        .eq(dto.getTreeId() != null, FertilizationRecord::getTreeId, dto.getTreeId())
                        .eq(dto.getFertilizerId() != null, FertilizationRecord::getFertilizerId, dto.getFertilizerId())
                        .ge(dto.getStartDate() != null, FertilizationRecord::getFertilizeDate, dto.getStartDate())
                        .le(dto.getEndDate() != null, FertilizationRecord::getFertilizeDate, dto.getEndDate())
                        .orderByDesc(FertilizationRecord::getFertilizeDate)
        );
        return ResultVO.success(PageVO.of(page.getTotal(), dto.getPage(), dto.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<FertilizationRecord> getById(@PathVariable Long id) {
        return ResultVO.success(recordService.getById(id));
    }

    @PostMapping
    public ResultVO<?> add(@Valid @RequestBody FertilizationRecordDTO dto) {
        var record = new FertilizationRecord();
        BeanUtils.copyProperties(dto, record);
        recordService.save(record);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        recordService.removeById(id);
        return ResultVO.success();
    }

    @GetMapping("/stats")
    public ResultVO<?> stats() {
        return ResultVO.success(recordService.getStats());
    }
}
