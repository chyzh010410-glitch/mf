package com.mf.fertilizer.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderShipDTO {
    @NotBlank private String logisticsCompany;
    @NotBlank private String logisticsNo;
}
