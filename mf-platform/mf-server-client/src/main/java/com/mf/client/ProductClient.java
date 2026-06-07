package com.mf.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(name = "mf-product")
public interface ProductClient {

    /** 查询商品基本信息 */
    @GetMapping("/client/products/{id}")
    Map<String, Object> getProduct(@PathVariable Long id);

    /** 批量查询商品 */
    @GetMapping("/client/products")
    Map<String, Object> listProducts(@RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) String productType);
}
