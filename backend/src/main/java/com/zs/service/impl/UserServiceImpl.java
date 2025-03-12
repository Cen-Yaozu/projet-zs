package com.zs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.User;
import com.zs.mapper.UserMapper;
import com.zs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.zs.common.response.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (lambdaQuery().eq(User::getUsername, user.getUsername()).one() != null) {
            return false;
        }
        
        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return save(user);
    }

    @Override
    public LoginResponse login(String username, String password, String role) {
        // 使用 MyBatis-Plus 的 Lambda 查询
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .one();
                
        if (user == null || user.getStatus() == 0) {
            return null;
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // 生成JWT token
        String token = generateJwtToken(user);

        // 返回登录响应
        return new LoginResponse(token, user);
    }

    private String generateJwtToken(User user) {
        Date expiration = new Date(System.currentTimeMillis() + jwtExpiration);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    @Override
    public boolean deleteUser(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            // 如果密码被修改，需要重新加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return updateById(user);
    }

    @Override
    public User getUserById(Long id) {
        return getById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return list();
    }

    @Override
    public User getUserByUsername(String username) {
        return lambdaQuery()
                .eq(User::getUsername, username)
                .one();
    }

    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        // 首先查找用户
        User user = getUserByUsername(username);
        
        // 如果用户不存在或者状态为禁用，返回false
        if (user == null || user.getStatus() == 0) {
            return false;
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        
        // 更新为新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        
        // 保存更新
        return updateById(user);
    }
}