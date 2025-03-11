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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

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
}