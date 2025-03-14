package com.zs.service;

import com.zs.entity.User;
import java.util.List;
import com.zs.common.response.LoginResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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

    // 更新密码
    boolean updatePassword(String username, String oldPassword, String newPassword);

    // 重置管理员密码
    boolean resetAdminPassword();

    @Value("${file.upload.path}")
    String getFileUploadPath();

    String uploadAvatar(MultipartFile file, String username) throws IOException;
}