package com.mf.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "mf-basic")
public interface BasicClient {

    /** 获取树种列表 */
    @GetMapping("/tree/species")
    List<String> getTreeSpecies();

    /** 获取肥料列表 */
    @GetMapping("/fertilizer/list")
    List<Map<String, Object>> getFertilizerList();

    /** 获取系统日志 */
    @GetMapping("/admin/logs")
    Map<String, Object> getLogs(@RequestParam("page") Integer page,
                                 @RequestParam("size") Integer size,
                                 @RequestParam(required = false) String module);
}
