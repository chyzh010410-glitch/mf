package com.mf.server.config;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

@Configuration
public class MyBatisPlusConfig {
    @Bean public MybatisPlusInterceptor interceptor() {
        var i = new MybatisPlusInterceptor();
        i.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return i;
    }
    @Bean public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override public void insertFill(MetaObject mo) {
                this.strictInsertFill(mo,"createTime",LocalDateTime.class,LocalDateTime.now());
                this.strictInsertFill(mo,"updateTime",LocalDateTime.class,LocalDateTime.now());
                this.strictInsertFill(mo,"deleted",Integer.class,0);
            }
            @Override public void updateFill(MetaObject mo) {
                this.strictUpdateFill(mo,"updateTime",LocalDateTime.class,LocalDateTime.now());
            }
        };
    }
}
