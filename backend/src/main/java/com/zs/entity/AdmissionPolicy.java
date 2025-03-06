package com.zs.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 招生政策实体类
 */
@Data
public class AdmissionPolicy implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属学校ID
     */
    private Long schoolId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 招生类型（统招、自主招生等）
     */
    private String admissionType;

    /**
     * 招生批次
     */
    private String batch;

    /**
     * 计划招生人数
     */
    private Integer planCount;

    /**
     * 最低录取分数
     */
    private Integer minScore;

    /**
     * 平均分
     */
    private Integer avgScore;

    /**
     * 政策说明
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}