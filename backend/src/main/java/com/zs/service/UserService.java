package com.zs.service;

import com.zs.entity.User;
import java.util.List;
import com.zs.common.response.LoginResponse;

public interface UserService {
    // 用户注册
    boolean register(User user);

    // 用户登录
    LoginResponse login(String username, String password, String role);

    // 删除用户
    boolean deleteUser(Long id);

    // 更新用户信息
    boolean updateUser(User user);

    // 根据ID获取用户
    User getUserById(Long id);

    // 获取所有用户
    List<User> getAllUsers();

    // 根据用户名查询用户
    User getUserByUsername(String username);
}