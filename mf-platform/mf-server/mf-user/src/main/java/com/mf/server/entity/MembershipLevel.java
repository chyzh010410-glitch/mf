package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data @EqualsAndHashCode(callSuper=true) @TableName("membership_level")
public class MembershipLevel extends BaseEntity {
    private String name; private Integer level; private Integer minPoints;
    private BigDecimal discountRate; private String icon; private String description;
}
