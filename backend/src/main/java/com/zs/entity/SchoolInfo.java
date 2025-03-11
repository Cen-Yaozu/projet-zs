package com.zs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学校信息实体类
 */
@Data
@TableName("school_info")
public class SchoolInfo implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学校名称
     */
    private String name;

    /**
     * 学校代码
     */
    private String code;

    /**
     * 学校类型（本科、专科等）
     */
    private String type;

    /**
     * 学校层次（985、211等）
     */
    private String level;

    /**
     * 所在省份
     */
    private String province;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 学校简介
     */
    private String description;

    /**
     * 官网地址
     */
    private String website;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}