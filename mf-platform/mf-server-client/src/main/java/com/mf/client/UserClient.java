package com.mf.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "mf-user")
public interface UserClient {

    /** 获取用户的收货地址列表 */
    @GetMapping("/client/addresses")
    List<Map<String, Object>> getAddresses(@RequestHeader("X-User-Id") Long userId);

    /** 获取用户基本信息 */
    @GetMapping("/client/user/profile")
    Map<String, Object> getProfile(@RequestHeader("X-User-Id") Long userId);
}
