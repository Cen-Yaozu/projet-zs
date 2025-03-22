package com.zs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 专业分数线实体类
 */
@Data
@TableName("major_score")
public class MajorScore implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学校ID
     */
    private Long schoolId;
    
    /**
     * 专业ID
     */
    private Long majorId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 招生省份
     */
    private String province;

    /**
     * 计划招生人数
     */
    private Integer planNumber;

    /**
     * 最低录取分数
     */
    private Integer minScore;
    
    /**
     * 最高录取分数
     */
    private Integer maxScore;
    
    /**
     * 省控线
     */
    private Integer provinceControlLine;
    
    /**
     * 首选科目
     */
    private String firstSubject;
    
    /**
     * 再选科目
     */
    private String secondSubject;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 