package com.mf.fertilizer.constant;

public interface RedisKey {

    String LOGIN_TOKEN = "login:token:";
    String TREE_LIST = "cache:tree:list";
    String FERTILIZER_LIST = "cache:fertilizer:list";
    String RECOMMEND = "cache:recommend:";
    String STATS = "cache:stats";

    /** 令牌过期时间(天) */
    int TOKEN_EXPIRE_DAYS = 7;

    /** 列表缓存过期时间(分钟) */
    int LIST_CACHE_MINUTES = 30;

    /** 推荐缓存过期时间(小时) */
    int RECOMMEND_CACHE_HOURS = 24;
}
