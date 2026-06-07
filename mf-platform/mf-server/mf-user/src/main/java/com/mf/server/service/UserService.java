package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.User;
import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(String username, String password);
}
