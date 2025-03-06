package com.zs.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 专业信息实体类
 */
@Data
public class MajorInfo implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 专业代码
     */
    private String code;

    /**
     * 所属学校ID
     */
    private Long schoolId;

    /**
     * 学科门类
     */
    private String category;

    /**
     * 专业类别
     */
    private String type;

    /**
     * 学制年限
     */
    private String duration;

    /**
     * 学位授予
     */
    private String degree;

    /**
     * 专业介绍
     */
    private String description;

    /**
     * 就业方向
     */
    private String careerProspects;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}