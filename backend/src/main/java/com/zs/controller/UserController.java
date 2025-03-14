package com.zs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zs.common.response.ApiResponse;
import com.zs.entity.User;
import com.zs.service.UserService;
import com.zs.common.response.LoginResponse;
import java.util.List;
import com.zs.common.request.*;
import com.zs.common.request.PasswordUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public ResponseEntity<?> register(@Parameter(description = "用户信息", required = true) @RequestBody User user) {
        boolean success = userService.register(user);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录并返回token")
    public ResponseEntity<?> login(@Parameter(description = "登录信息", required = true) @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword(),
                loginRequest.getRole());
        if (response != null) {
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("用户名或密码错误"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public ResponseEntity<?> deleteUser(@Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    @Operation(summary = "更新用户", description = "更新用户信息")
    public ResponseEntity<?> updateUser(@Parameter(description = "用户信息", required = true) @RequestBody User user) {
        boolean success = userService.updateUser(user);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public ResponseEntity<User> getUserById(@Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "获取所有用户", description = "获取系统中所有用户信息")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/username/{username}")
    @Operation(summary = "根据用户名查询", description = "根据用户名查询用户信息")
    public ResponseEntity<User> getUserByUsername(
            @Parameter(description = "用户名", required = true) @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/update-password")
    @Operation(summary = "更新密码", description = "根据用户名、旧密码验证并更新新密码")
    public ResponseEntity<?> updatePassword(
            @Parameter(description = "密码更新信息", required = true) @RequestBody PasswordUpdateRequest request) {
        boolean success = userService.updatePassword(
                request.getUsername(), 
                request.getOldPassword(),
                request.getNewPassword());
        
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("密码更新成功"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("密码更新失败，请检查用户名和旧密码"));
        }
    }

    // 临时端点：重置管理员密码
    @PostMapping("/reset-admin")
    @Operation(summary = "重置管理员密码", description = "将管理员密码重置为默认密码")
    public ResponseEntity<?> resetAdminPassword() {
        boolean success = userService.resetAdminPassword();
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("管理员密码已重置为: admin123"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("重置密码失败"));
        }
    }

    @PostMapping("/avatar")
    @Operation(summary = "上传头像", description = "上传并更新用户头像")
    public ResponseEntity<?> uploadAvatar(
            @Parameter(description = "头像文件", required = true) @RequestParam("file") MultipartFile file,
            @Parameter(description = "用户名", required = true) @RequestParam("username") String username) {
        try {
            String avatarUrl = userService.uploadAvatar(file, username);
            return ResponseEntity.ok(ApiResponse.success(new HashMap<String, String>() {{
                put("avatar", avatarUrl);
            }}));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("头像上传失败: " + e.getMessage()));
        }
    }
}