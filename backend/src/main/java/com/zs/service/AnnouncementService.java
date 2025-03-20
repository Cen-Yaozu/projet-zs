package com.zs.service;

import com.zs.entity.Announcement;

import java.util.List;

/**
 * 通知公告服务接口
 */
public interface AnnouncementService {
    
    /**
     * 获取所有通知公告
     * @return 通知公告列表
     */
    List<Announcement> findAll();
    
    /**
     * 根据ID获取通知公告
     * @param id 通知公告ID
     * @return 通知公告对象
     */
    Announcement findById(Long id);
    
    /**
     * 获取有效的通知公告
     * @return 有效的通知公告列表
     */
    List<Announcement> findActive();
    
    /**
     * 根据类型获取通知公告
     * @param type 通知公告类型
     * @return 通知公告列表
     */
    List<Announcement> findByType(String type);
    
    /**
     * 添加通知公告
     * @param announcement 通知公告对象
     * @return 添加后的通知公告对象
     */
    Announcement add(Announcement announcement);
    
    /**
     * 更新通知公告
     * @param announcement 通知公告对象
     * @return 更新后的通知公告对象
     */
    Announcement update(Announcement announcement);
    
    /**
     * 删除通知公告
     * @param id 通知公告ID
     * @return 是否删除成功
     */
    boolean delete(Long id);
} 