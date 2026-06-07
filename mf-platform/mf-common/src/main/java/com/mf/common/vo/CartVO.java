package com.mf.common.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class CartVO implements Serializable {
    private List<CartItemVO> items;
    private Integer totalCount;
    private BigDecimal totalAmount;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CartItemVO implements Serializable {
        private Long id; private Long userId; private Long productId;
        private String productName; private BigDecimal price;
        private Integer stock; private Integer quantity; private Integer selected;
    }
}
