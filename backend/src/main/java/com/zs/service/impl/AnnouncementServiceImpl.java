package com.zs.service.impl;

import com.zs.entity.Announcement;
import com.zs.mapper.AnnouncementMapper;
import com.zs.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告服务实现类
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public List<Announcement> findAll() {
        return announcementMapper.findAll();
    }

    @Override
    public Announcement findById(Long id) {
        return announcementMapper.findById(id);
    }

    @Override
    public List<Announcement> findActive() {
        return announcementMapper.findActive();
    }

    @Override
    public List<Announcement> findByType(String type) {
        return announcementMapper.findByType(type);
    }

    @Override
    public List<Announcement> findByCategory(String category) {
        return announcementMapper.findByCategory(category);
    }

    @Override
    @Transactional
    public Announcement add(Announcement announcement) {
        // 设置默认值
        if (announcement.getStatus() == null) {
            announcement.setStatus(1);
        }
        if (announcement.getType() == null || announcement.getType().isEmpty()) {
            announcement.setType("GENERAL");
        }
        if (announcement.getCategory() == null || announcement.getCategory().isEmpty()) {
            announcement.setCategory("GENERAL_NOTICE");
        }
        if (announcement.getPublishTime() == null) {
            announcement.setPublishTime(LocalDateTime.now());
        }
        
        announcementMapper.insert(announcement);
        return announcement;
    }

    @Override
    @Transactional
    public Announcement update(Announcement announcement) {
        // 更新前检查记录是否存在
        Announcement existingAnnouncement = announcementMapper.findById(announcement.getId());
        if (existingAnnouncement == null) {
            throw new RuntimeException("通知公告不存在");
        }
        
        announcementMapper.update(announcement);
        return announcementMapper.findById(announcement.getId());
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        // 删除前检查记录是否存在
        Announcement existingAnnouncement = announcementMapper.findById(id);
        if (existingAnnouncement == null) {
            throw new RuntimeException("通知公告不存在");
        }
        
        return announcementMapper.delete(id) > 0;
    }
} 