package com.mf.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "mf-order")
public interface OrderClient {

    /** 查询订单 */
    @GetMapping("/orders/{id}")
    Map<String, Object> getOrder(@PathVariable Long id);

    /** 取消订单 */
    @PostMapping("/orders/{id}/cancel")
    Map<String, Object> cancelOrder(@PathVariable Long id);
}
