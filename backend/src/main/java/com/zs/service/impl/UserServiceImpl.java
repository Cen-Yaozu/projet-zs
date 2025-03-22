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
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecretKey jwtSecretKey;

    @Value("${file.upload.path}")
    private String fileUploadPath;

    @Autowired
    private UserMapper userMapper;

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
        System.out.println("Attempting login with - Username: " + username);
        
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

        // 验证密码
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        System.out.println("Password verification result: " + passwordMatch);
        
        if (!passwordMatch) {
            System.out.println("Login failed: Password does not match");
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

    @Override
    public String uploadAvatar(MultipartFile file, String username) throws IOException {
        // 创建上传目录
        String uploadDir = fileUploadPath + "/avatars/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只允许上传图片文件");
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // 保存文件
        Path filePath = Paths.get(uploadDir + filename);
        Files.copy(file.getInputStream(), filePath);

        // 更新用户头像路径
        User user = getUserByUsername(username);
        if (user != null) {
            // 删除旧头像
            String oldAvatar = user.getAvatar();
            if (oldAvatar != null && !oldAvatar.equals("/static/images/logo5.png")) {
                String oldFilePath = fileUploadPath + oldAvatar.replace("/static", "");
                try {
                    Files.deleteIfExists(Paths.get(oldFilePath));
                } catch (IOException e) {
                    // 忽略删除旧文件失败的错误
                }
            }

            // 更新新头像
            user.setAvatar("/static/avatars/" + filename);
            updateById(user);
        }

        return "/static/avatars/" + filename;
    }

    @Override
    public boolean updateUserStatus(Long id, Boolean status) {
        User user = getById(id);
        if (user == null) {
            return false;
        }
        
        // 将布尔值转换为整数 (true -> 1, false -> 0)
        user.setStatus(status ? 1 : 0);
        return updateById(user);
    }
    
    @Override
    public boolean resetUserPassword(Long id) {
        User user = getById(id);
        if (user == null) {
            return false;
        }
        
        // 重置为默认密码 "123456"
        String defaultPassword = "123456";
        String encodedPassword = passwordEncoder.encode(defaultPassword);
        user.setPassword(encodedPassword);
        
        return updateById(user);
    }
}