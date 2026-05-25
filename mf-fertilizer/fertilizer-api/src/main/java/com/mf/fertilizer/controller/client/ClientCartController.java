package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.dto.client.CartAddDTO;
import com.mf.fertilizer.dto.client.CartUpdateDTO;
import com.mf.fertilizer.entity.ShoppingCartItem;
import com.mf.fertilizer.entity.Product;
import com.mf.fertilizer.service.ShoppingCartItemService;
import com.mf.fertilizer.service.ProductService;
import com.mf.fertilizer.vo.ResultVO;
import com.mf.fertilizer.vo.client.CartVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequestMapping("/client/cart")
@RequiredArgsConstructor
public class ClientCartController {

    private final ShoppingCartItemService cartService;
    private final ProductService productService;

    @GetMapping
    public ResultVO<CartVO> list() {
        Long userId = UserContext.getUserId();
        var items = cartService.lambdaQuery().eq(ShoppingCartItem::getUserId, userId).list();
        var vo = new CartVO();
        var itemVOs = new ArrayList<CartVO.CartItemVO>();
        int totalCount = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (var item : items) {
            var product = productService.getById(item.getProductId());
            if (product == null || product.getStatus() == 0) continue;
            var iv = new CartVO.CartItemVO();
            iv.setId(item.getId()); iv.setProductId(item.getProductId());
            iv.setProductName(product.getName()); iv.setProductImage(product.getCoverImage());
            iv.setProductType(product.getProductType()); iv.setStock(product.getStock());
            iv.setPrice(product.getPrice()); iv.setQuantity(item.getQuantity());
            iv.setSelected(item.getSelected());
            iv.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            itemVOs.add(iv);
            if (item.getSelected() == 1) {
                totalCount += item.getQuantity();
                totalAmount = totalAmount.add(iv.getSubtotal());
            }
        }
        vo.setItems(itemVOs); vo.setTotalCount(totalCount); vo.setTotalAmount(totalAmount);
        return ResultVO.success(vo);
    }

    @PostMapping
    public ResultVO<?> add(@Valid @RequestBody CartAddDTO dto) {
        Long userId = UserContext.getUserId();
        var existing = cartService.lambdaQuery()
                .eq(ShoppingCartItem::getUserId, userId)
                .eq(ShoppingCartItem::getProductId, dto.getProductId()).one();
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + dto.getQuantity());
            cartService.updateById(existing);
        } else {
            try {
                var item = new ShoppingCartItem();
                item.setUserId(userId);
                item.setProductId(dto.getProductId());
                item.setQuantity(dto.getQuantity());
                item.setSelected(1);
                cartService.save(item);
            } catch (org.springframework.dao.DuplicateKeyException e) {
                var dup = cartService.lambdaQuery()
                        .eq(ShoppingCartItem::getUserId, userId)
                        .eq(ShoppingCartItem::getProductId, dto.getProductId()).one();
                if (dup != null) {
                    dup.setQuantity(dup.getQuantity() + dto.getQuantity());
                    cartService.updateById(dup);
                }
            }
        }
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody CartUpdateDTO dto) {
        var item = cartService.getById(id);
        if (item == null) return ResultVO.fail(404, "购物车项不存在");
        if (dto.getQuantity() != null) item.setQuantity(dto.getQuantity());
        if (dto.getSelected() != null) item.setSelected(dto.getSelected());
        cartService.updateById(item);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        cartService.removeById(id);
        return ResultVO.success();
    }

    @DeleteMapping
    public ResultVO<?> clear() {
        Long userId = UserContext.getUserId();
        cartService.lambdaUpdate().eq(ShoppingCartItem::getUserId, userId).remove();
        return ResultVO.success();
    }
}
