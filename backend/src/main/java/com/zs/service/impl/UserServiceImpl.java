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
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecretKey jwtSecretKey;

    // 临时方法：重置管理员密码
    @Override
    public boolean resetAdminPassword() {
        User admin = lambdaQuery()
                .eq(User::getUsername, "admin")
                .one();
                
        if (admin == null) {
            return false;
        }
        
        // 重新加密 "admin123" 密码
        String newPasswordHash = passwordEncoder.encode("admin123");
        admin.setPassword(newPasswordHash);
        
        return updateById(admin);
    }

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
        System.out.println("=== Login Debug Info ===");
        System.out.println("Attempting login with - Username: " + username + ", Role: " + role);
        
        // 使用 MyBatis-Plus 的 Lambda 查询
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .one();
                
        if (user == null) {
            System.out.println("Login failed: User not found");
            return null;
        }
        
        if (user.getStatus() == 0) {
            System.out.println("Login failed: User is inactive");
            return null;
        }
        
        System.out.println("Found user in database:");
        System.out.println("- Username: " + user.getUsername());
        System.out.println("- Role: " + user.getRole());
        System.out.println("- Status: " + user.getStatus());
        System.out.println("- Stored password hash: " + user.getPassword());

        // 验证密码
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        System.out.println("Password verification result: " + passwordMatch);
        System.out.println("Input password: " + password);
        
        if (!passwordMatch) {
            System.out.println("Login failed: Password does not match");
            return null;
        }

        // 验证角色
        boolean roleMatch = user.getRole().equals(role);
        System.out.println("Role verification:");
        System.out.println("- Expected role: " + role);
        System.out.println("- Actual role: " + user.getRole());
        System.out.println("- Role matches: " + roleMatch);
        
        if (!roleMatch) {
            System.out.println("Login failed: Role does not match");
            return null;
        }

        System.out.println("Login successful!");
        
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
                .signWith(jwtSecretKey)
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