package com.mf.fertilizer.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FertilizerQueryDTO extends PageDTO {

    private String name;

    private String type;
}
