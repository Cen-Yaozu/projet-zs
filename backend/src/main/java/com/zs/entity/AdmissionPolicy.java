package com.zs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 招生政策实体类
 */
@Data
@TableName("admission_policy")
public class AdmissionPolicy implements Serializable {
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
     * 科类（理科、文科等）
     */
    private String category;

    /**
     * 政策详细内容
     */
    private String policyContent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}