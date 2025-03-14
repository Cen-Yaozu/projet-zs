package com.zs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;  // 用户头像URL
    private Integer status;  // 0: 禁用, 1: 正常
    private String role;    // 用户角色: ROLE_USER, ROLE_ADMIN 等
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}