package com.mf.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "mf-content")
public interface ContentClient {

    /** 获取百科词条 */
    @GetMapping("/client/encyclopedia/{id}")
    Map<String, Object> getEncyclopediaEntry(@PathVariable Long id);

    /** 获取科普文章 */
    @GetMapping("/client/articles/{id}")
    Map<String, Object> getArticle(@PathVariable Long id);

    /** 获取未读消息数 */
    @GetMapping("/client/messages/unread-count")
    Map<String, Object> getUnreadCount(@RequestHeader("X-User-Id") Long userId);
}
