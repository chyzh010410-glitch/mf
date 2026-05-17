package com.mf.fertilizer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("platform_config")
public class PlatformConfig extends BaseEntity {
    private String configKey;
    private String configValue;
    private String configGroup;
    private String description;
    private String valueType;
}
