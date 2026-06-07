package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;
import java.math.BigDecimal; import java.time.LocalDateTime;

@Data @EqualsAndHashCode(callSuper=true) @TableName("`order`")
public class OrderEntity extends BaseEntity {
    private String orderNo; private Long userId; private BigDecimal totalAmount;
    private BigDecimal freightAmount; private BigDecimal discountAmount;
    private BigDecimal payAmount; private String status; private String paymentMethod;
    private String addressSnapshot; private String userRemark; 
     private LocalDateTime payTime; private LocalDateTime shipTime;
    private LocalDateTime completeTime; private LocalDateTime cancelTime; private String cancelReason; private String adminRemark;
}
