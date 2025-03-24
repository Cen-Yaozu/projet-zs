package com.zs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告实体类
 */
@Data
public class Announcement {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 公告标题
     */
    private String title;
    
    /**
     * 公告内容
     */
    private String content;
    
    /**
     * 公告图片URL
     */
    private String imageUrl;
    
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;
    
    /**
     * 公告类型：GENERAL-普通公告，IMPORTANT-重要公告
     */
    private String type;
    
    /**
     * 公告类别：GENERAL_NOTICE-招生公告，ADMISSION_RULES-招生章程，ADMISSION_POLICY-招生政策，HISTORICAL_REFERENCE-往年参考
     */
    private String category = "GENERAL_NOTICE";
    
    /**
     * 创建人ID
     */
    private Long createUserId;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 