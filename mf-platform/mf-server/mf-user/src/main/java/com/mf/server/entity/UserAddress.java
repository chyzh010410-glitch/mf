package com.mf.server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mf.common.base.BaseEntity;
import lombok.Data; import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper=true) @TableName("user_address")
public class UserAddress extends BaseEntity {
    private Long userId; private String receiverName; private String receiverPhone;
    private String province; private String city; private String district;
    private String detail; private Integer isDefault; private String postalCode; private String tag;
}
