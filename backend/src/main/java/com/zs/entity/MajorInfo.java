package com.zs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 专业信息实体类
 */
@Data
@TableName("major_info")
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
     * 就业前景
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