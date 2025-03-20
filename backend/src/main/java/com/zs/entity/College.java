package com.zs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 院系信息实体类
 */
@Data
@TableName("college")
public class College implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 院系名称
     */
    private String name;

    /**
     * 学历层次（本科、专科等）
     */
    private String degree;

    /**
     * 学制（年）
     */
    private String duration;

    /**
     * 院系介绍
     */
    private String description;

    /**
     * 所属学校ID
     */
    private Long schoolId;

    /**
     * 院系Logo图片URL
     */
    private String logoUrl;

    /**
     * 院系横幅图片URL
     */
    private String bannerUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 