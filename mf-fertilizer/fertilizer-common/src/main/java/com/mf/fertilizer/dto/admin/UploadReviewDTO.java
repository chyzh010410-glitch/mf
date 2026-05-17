package com.mf.fertilizer.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UploadReviewDTO {
    @NotBlank private String status;
    private String reviewComment;
    private Long encyclopediaId;
}
