package com.mf.server.controller;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.service.ProductService;
import com.mf.server.service.ShoppingCartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/client/cart") @RequiredArgsConstructor
public class CartController {
    private final ShoppingCartItemService cartService;
    private final ProductService productService;

    @GetMapping
    public ResultVO<com.mf.common.vo.CartVO> list() {
        Long userId=UserContext.getUserId();
        var items = cartService.listByUser(userId);
        var cartVO = new com.mf.common.vo.CartVO();
        var itemVOs = new java.util.ArrayList<com.mf.common.vo.CartVO.CartItemVO>();
        for(var item : items){
            var vo = new com.mf.common.vo.CartVO.CartItemVO();
            vo.setId(item.getId()); vo.setUserId(item.getUserId()); vo.setProductId(item.getProductId());
            vo.setQuantity(item.getQuantity()); vo.setSelected(item.getSelected());
            var product = productService.getById(item.getProductId());
            if(product != null){
                vo.setProductName(product.getName()); vo.setPrice(product.getPrice()); vo.setStock(product.getStock());
            }
            itemVOs.add(vo);
        }
        cartVO.setItems(itemVOs);
        cartVO.setTotalCount(itemVOs.size());
        return ResultVO.success(cartVO);
    }

    @PostMapping
    public ResultVO<?> add(@RequestBody Map<String,Object> body) {
        Long userId=UserContext.getUserId();
        Long productId=Long.valueOf(body.get("productId").toString());
        int qty=body.get("quantity")!=null?Integer.parseInt(body.get("quantity").toString()):1;
        cartService.restoreOrAdd(userId, productId, qty);
        return ResultVO.success();
    }

    @PutMapping("/{id}") public ResultVO<?> update(@PathVariable Long id, @RequestBody Map<String,Object> b) {
        var item=cartService.getById(id); if(b.containsKey("quantity")) item.setQuantity(Integer.parseInt(b.get("quantity").toString()));
        if(b.containsKey("selected")) item.setSelected(Integer.parseInt(b.get("selected").toString())); cartService.updateById(item); return ResultVO.success(); }
    @DeleteMapping("/{id}") public ResultVO<?> delete(@PathVariable Long id) { cartService.removeById(id); return ResultVO.success(); }
}
