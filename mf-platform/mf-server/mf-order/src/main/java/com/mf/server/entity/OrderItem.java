package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data @EqualsAndHashCode(callSuper=true) @TableName("order_item")
public class OrderItem extends BaseEntity {
    private Long orderId; private String orderNo; private Long productId;
    private String productName; private String productImage; private BigDecimal price;
    private Integer quantity; private BigDecimal totalPrice; private String productAttrs;
}
