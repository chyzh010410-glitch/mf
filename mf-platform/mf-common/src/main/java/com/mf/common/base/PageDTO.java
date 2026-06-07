package com.mf.common.base;
import jakarta.validation.constraints.Min; import lombok.Data;

@Data
public class PageDTO { @Min(1) private Integer page=1; @Min(1) private Integer size=10; }
