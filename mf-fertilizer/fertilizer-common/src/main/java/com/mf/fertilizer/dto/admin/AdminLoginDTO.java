package com.mf.fertilizer.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminLoginDTO {
    @NotBlank private String username;
    @NotBlank private String password;
}
