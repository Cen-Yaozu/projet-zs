package com.zs.controller;

import com.zs.common.response.ApiResponse;
import com.zs.entity.Announcement;
import com.zs.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告控制器
 */
@RestController
@RequestMapping("/api/announcements")
@Tag(name = "通知公告接口", description = "提供通知公告的增删改查功能")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 获取所有通知公告
     */
    @GetMapping
    @Operation(summary = "获取所有通知公告", description = "获取所有通知公告，按发布时间降序排列")
    public ResponseEntity<?> getAllAnnouncements() {
        try {
            List<Announcement> announcements = announcementService.findAll();
            return ResponseEntity.ok(ApiResponse.success(announcements));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取通知公告失败: " + e.getMessage()));
        }
    }

    /**
     * 获取有效的通知公告
     */
    @GetMapping("/active")
    @Operation(summary = "获取有效的通知公告", description = "获取状态为有效且未过期的通知公告，重要公告优先")
    public ResponseEntity<?> getActiveAnnouncements() {
        try {
            List<Announcement> announcements = announcementService.findActive();
            return ResponseEntity.ok(ApiResponse.success(announcements));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取有效通知公告失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取通知公告
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取通知公告", description = "根据ID获取特定的通知公告详情")
    public ResponseEntity<?> getAnnouncementById(
            @Parameter(description = "通知公告ID", required = true) 
            @PathVariable Long id) {
        try {
            Announcement announcement = announcementService.findById(id);
            if (announcement == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(ApiResponse.success(announcement));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取通知公告详情失败: " + e.getMessage()));
        }
    }

    /**
     * 根据类型获取通知公告
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取通知公告", description = "获取特定类型的通知公告，按发布时间降序排列")
    public ResponseEntity<?> getAnnouncementsByType(
            @Parameter(description = "通知公告类型", required = true) 
            @PathVariable String type) {
        try {
            List<Announcement> announcements = announcementService.findByType(type);
            return ResponseEntity.ok(ApiResponse.success(announcements));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取通知公告失败: " + e.getMessage()));
        }
    }

    /**
     * 根据类别获取通知公告
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "根据类别获取通知公告", description = "获取特定类别的通知公告，如招生公告、招生章程等，按发布时间降序排列")
    public ResponseEntity<?> getAnnouncementsByCategory(
            @Parameter(description = "通知公告类别", required = true) 
            @PathVariable String category) {
        try {
            List<Announcement> announcements = announcementService.findByCategory(category);
            return ResponseEntity.ok(ApiResponse.success(announcements));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取通知公告失败: " + e.getMessage()));
        }
    }

    /**
     * 添加通知公告
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "添加通知公告", description = "添加新的通知公告，需要管理员权限")
    public ResponseEntity<?> addAnnouncement(
            @Parameter(description = "通知公告信息", required = true) 
            @RequestBody Announcement announcement) {
        try {
            Announcement addedAnnouncement = announcementService.add(announcement);
            return ResponseEntity.ok(ApiResponse.success(addedAnnouncement));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("添加通知公告失败: " + e.getMessage()));
        }
    }

    /**
     * 更新通知公告
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新通知公告", description = "更新已有的通知公告信息，需要管理员权限")
    public ResponseEntity<?> updateAnnouncement(
            @Parameter(description = "通知公告ID", required = true) 
            @PathVariable Long id,
            @Parameter(description = "更新后的通知公告信息", required = true) 
            @RequestBody Announcement announcement) {
        try {
            announcement.setId(id);
            Announcement updatedAnnouncement = announcementService.update(announcement);
            return ResponseEntity.ok(ApiResponse.success(updatedAnnouncement));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新通知公告失败: " + e.getMessage()));
        }
    }

    /**
     * 删除通知公告
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除通知公告", description = "删除特定的通知公告，需要管理员权限")
    public ResponseEntity<?> deleteAnnouncement(
            @Parameter(description = "通知公告ID", required = true) 
            @PathVariable Long id) {
        try {
            boolean success = announcementService.delete(id);
            if (success) {
                return ResponseEntity.ok(ApiResponse.success("通知公告删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("通知公告删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("删除通知公告失败: " + e.getMessage()));
        }
    }
} 