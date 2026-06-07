package com.mf.common.constant;

public interface RedisKey {
    String LOGIN_TOKEN = "login:token:";
    String CLIENT_TOKEN = "client:token:";
    String CACHE_TREE_LIST = "cache:tree:list";
    String CACHE_FERTILIZER_LIST = "cache:fertilizer:list";
    String CACHE_RECOMMEND = "cache:recommend:";
    int TOKEN_EXPIRE_DAYS = 7;
    int LIST_CACHE_MINUTES = 30;
    int RECOMMEND_CACHE_HOURS = 24;
}
