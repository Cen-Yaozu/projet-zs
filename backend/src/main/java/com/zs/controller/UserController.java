package com.zs.controller;

import com.zs.common.response.*;
import com.zs.entity.User;
import com.zs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.zs.common.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "注册新用户")
    public ResponseEntity<?> register(@ApiParam(value = "用户信息", required = true) @RequestBody User user) {
        boolean success = userService.register(user);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录并返回token")
    public ResponseEntity<?> login(@ApiParam(value = "登录信息", required = true) @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword(),
                loginRequest.getRole());
        if (response != null) {
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("用户名或密码错误"));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "根据用户ID删除用户")
    public ResponseEntity<?> deleteUser(@ApiParam(value = "用户ID", required = true) @PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    @ApiOperation(value = "更新用户", notes = "更新用户信息")
    public ResponseEntity<?> updateUser(@ApiParam(value = "用户信息", required = true) @RequestBody User user) {
        boolean success = userService.updateUser(user);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户ID获取用户信息")
    public ResponseEntity<User> getUserById(@ApiParam(value = "用户ID", required = true) @PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @ApiOperation(value = "获取所有用户", notes = "获取系统中所有用户信息")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/username/{username}")
    @ApiOperation(value = "根据用户名查询", notes = "根据用户名查询用户信息")
    public ResponseEntity<User> getUserByUsername(
            @ApiParam(value = "用户名", required = true) @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
}