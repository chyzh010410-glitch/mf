package com.mf.fertilizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FertilizationRuleDTO {

    @NotBlank(message = "适用树种不能为空")
    private String species;

    @NotNull(message = "最小树龄不能为空")
    private Integer ageMin;

    @NotNull(message = "最大树龄不能为空")
    private Integer ageMax;

    @NotBlank(message = "适用季节不能为空")
    private String season;

    @NotNull(message = "推荐肥料ID不能为空")
    private Long fertilizerId;

    @NotNull(message = "推荐用量不能为空")
    private BigDecimal recommendAmount;

    @NotBlank(message = "施肥方式不能为空")
    private String method;

    private Integer priority;

    private String remark;
}
