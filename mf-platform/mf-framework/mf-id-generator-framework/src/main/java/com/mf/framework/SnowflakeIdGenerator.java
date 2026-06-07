package com.mf.framework;

import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式雪花 ID 生成器。
 * 每个服务实例通过 workerId 区分，保证全局唯一。
 * workerId 从 0 开始，多实例部署时通过环境变量或配置中心分配不同值。
 */
@Configuration
public class SnowflakeIdGenerator {

    @Bean
    public cn.hutool.core.lang.Snowflake snowflake() {
        // workerId 和 datacenterId 在生产环境应从配置中心读取
        long workerId = Long.parseLong(System.getProperty("snowflake.workerId", "1"));
        long datacenterId = Long.parseLong(System.getProperty("snowflake.datacenterId", "1"));
        return IdUtil.getSnowflake(workerId, datacenterId);
    }
}
