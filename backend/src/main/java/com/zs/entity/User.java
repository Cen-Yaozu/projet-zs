package com.zs.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private Integer status;  // 0: 禁用, 1: 正常
    private String role;    // 用户角色: ROLE_USER, ROLE_ADMIN 等
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}