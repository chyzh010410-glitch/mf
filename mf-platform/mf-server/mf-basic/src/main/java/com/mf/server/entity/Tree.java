package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data @EqualsAndHashCode(callSuper=true) @TableName("tree")
public class Tree extends BaseEntity {
    private String species; private String variety; private Integer age;
    private LocalDate plantDate; private String location; private java.math.BigDecimal area;
    private Integer quantity; private String status; private String remark;
}
