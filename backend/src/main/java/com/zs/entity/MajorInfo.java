package com.zs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专业信息实体类
 */
@Data
@TableName(value = "major_info", autoResultMap = true)
public class MajorInfo implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属学校ID
     */
    private Long schoolId;
    
    /**
     * 所属院系ID
     */
    private Long collegeId;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 专业代码
     */
    private String code;

    /**
     * 学位类型
     */
    private String degree;

    /**
     * 学制
     */
    private String duration;

    /**
     * 专业介绍
     */
    private String description;

    /**
     * 专业培养目标
     */
    private String trainingObjectives;
    
    /**
     * 主要课程
     */
    private String mainCourses;

    /**
     * 就业前景
     */
    private String careerProspects;
    
    /**
     * 专业图标URL
     */
    private String iconUrl;
    
    /**
     * 专业相关图片URL列表，以JSON数组形式存储
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}