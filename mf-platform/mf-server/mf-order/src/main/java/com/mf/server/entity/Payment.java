package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;
import java.math.BigDecimal; import java.time.LocalDateTime;

@Data @EqualsAndHashCode(callSuper=true) @TableName("payment")
public class Payment extends BaseEntity {
    private Long userId; private Long orderId; private String orderNo;
    private String payMethod; private BigDecimal amount; private String status;
    private String tradeNo; private BigDecimal refundAmount;
    private LocalDateTime payTime; private LocalDateTime refundTime;
    private String rawResponse; private String remark;
}
