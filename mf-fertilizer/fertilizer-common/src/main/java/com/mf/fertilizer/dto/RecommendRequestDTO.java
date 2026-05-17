package com.mf.fertilizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecommendRequestDTO {

    @NotBlank(message = "树种不能为空")
    private String species;

    @NotNull(message = "树龄不能为空")
    private Integer age;

    /** 不传则默认当前季节 */
    private String season;
}
