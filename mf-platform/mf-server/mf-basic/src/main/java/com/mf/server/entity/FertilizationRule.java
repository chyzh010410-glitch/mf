package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data @EqualsAndHashCode(callSuper=true) @TableName("fertilization_rule")
public class FertilizationRule extends BaseEntity {
    private String species; private Integer ageMin; private Integer ageMax;
    private String season; private Long fertilizerId; private BigDecimal recommendAmount;
    private String method; private Integer priority; private String remark;
}
