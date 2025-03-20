package com.zs.controller;

import com.zs.common.response.ApiResponse;
import com.zs.entity.College;
import com.zs.entity.MajorInfo;
import com.zs.service.CollegeService;
import com.zs.service.FileUploadService;
import com.zs.service.MajorInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用文件上传控制器
 * 提供统一的文件上传、批量上传和删除功能
 */
@RestController
@RequestMapping("/api/upload")
@Tag(name = "文件上传接口")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
    private MajorInfoService majorInfoService;
    
    @Autowired
    private CollegeService collegeService;
    
    /**
     * 单文件上传
     * @param directory 目标目录
     * @param file 上传的文件
     * @return 文件访问URL
     */
    @PostMapping
    @Operation(summary = "上传文件", description = "通用文件上传接口，根据指定目录保存文件")
    public ResponseEntity<?> upload(
            @Parameter(description = "目标目录", required = false) 
            @RequestParam(value = "directory", required = false, defaultValue = "common") String directory,
            @Parameter(description = "文件", required = true) 
            @RequestParam("file") MultipartFile file) {
        try {
            String url = fileUploadService.uploadImage(file, directory);
            
            Map<String, Object> result = new HashMap<>();
            result.put("url", url);
            result.put("code", 200);
            result.put("message", "上传成功");
            
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            result.put("data", data);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }

    /**
     * 批量上传文件
     * @param directory 目标目录
     * @param files 上传的文件列表
     * @return 文件访问URL列表
     */
    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "批量上传文件", description = "通用批量文件上传接口，根据指定目录保存多个文件")
    public ResponseEntity<?> uploadBatch(
            @Parameter(description = "目标目录", required = false) 
            @RequestParam(value = "directory", required = false, defaultValue = "common") String directory,
            @Parameter(description = "文件列表", required = true) 
            @RequestParam("files") MultipartFile[] files) {
        try {
            String[] urls = fileUploadService.uploadMultipleImages(files, directory);
            
            Map<String, String[]> result = new HashMap<>();
            result.put("urls", urls);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("文件批量上传失败: " + e.getMessage()));
        }
    }

    /**
     * 删除文件
     * @param url 文件URL
     * @return 删除操作结果
     */
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除文件", description = "根据文件URL删除已上传的文件")
    public ResponseEntity<?> delete(
            @Parameter(description = "文件URL", required = true) 
            @RequestParam("url") String url) {
        try {
            boolean deleted = fileUploadService.deleteImage(url);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("文件删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("文件删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("文件删除异常: " + e.getMessage()));
        }
    }
    
    // ======== 专业图片上传接口 ========
    
    /**
     * 上传专业图标
     */
    @PostMapping("/major/image/icon/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "上传专业图标", description = "上传并更新专业图标，需要管理员权限")
    public ResponseEntity<?> uploadMajorIcon(
            @Parameter(description = "专业ID", required = true) @PathVariable Long id,
            @Parameter(description = "图标文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            // 1. 检查专业是否存在
            MajorInfo majorInfo = majorInfoService.getById(id);
            if (majorInfo == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("专业不存在"));
            }

            // 2. 删除旧图标
            if (majorInfo.getIconUrl() != null) {
                fileUploadService.deleteImage(majorInfo.getIconUrl());
            }

            // 3. 上传新图标
            String iconUrl = fileUploadService.uploadImage(file, "major/icon");
            
            // 4. 更新专业信息
            majorInfo.setIconUrl(iconUrl);
            majorInfoService.updateById(majorInfo);

            // 5. 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("iconUrl", iconUrl);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("专业图标上传失败: " + e.getMessage()));
        }
    }

    /**
     * 上传专业图片
     */
    @PostMapping("/major/image/images/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "上传专业图片", description = "上传并更新专业相关图片，需要管理员权限")
    public ResponseEntity<?> uploadMajorImages(
            @Parameter(description = "专业ID", required = true) @PathVariable Long id,
            @Parameter(description = "图片文件", required = true) @RequestParam("files") MultipartFile[] files) {
        try {
            // 1. 检查专业是否存在
            MajorInfo majorInfo = majorInfoService.getById(id);
            if (majorInfo == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("专业不存在"));
            }

            // 2. 上传新图片
            String[] imageUrls = fileUploadService.uploadMultipleImages(files, "major/images");
            
            // 3. 合并新图片和现有图片
            List<String> imageList = new ArrayList<>();
            if (majorInfo.getImages() != null) {
                imageList.addAll(majorInfo.getImages());
            }
            imageList.addAll(Arrays.asList(imageUrls));
            
            // 4. 更新专业信息
            majorInfo.setImages(imageList);
            majorInfoService.updateById(majorInfo);

            // 5. 返回结果
            Map<String, List<String>> result = new HashMap<>();
            result.put("images", imageList);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("专业图片上传失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除专业某张图片
     */
    @DeleteMapping("/major/image/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除专业图片", description = "删除专业的某张图片，需要管理员权限")
    public ResponseEntity<?> deleteMajorImage(
            @Parameter(description = "专业ID", required = true) @PathVariable Long id,
            @Parameter(description = "图片URL", required = true) @RequestParam("imageUrl") String imageUrl) {
        try {
            // 1. 检查专业是否存在
            MajorInfo majorInfo = majorInfoService.getById(id);
            if (majorInfo == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("专业不存在"));
            }

            // 2. 检查图片是否存在
            List<String> images = majorInfo.getImages();
            if (images == null || !images.contains(imageUrl)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("该图片不存在"));
            }

            // 3. 删除图片文件
            boolean deleted = fileUploadService.deleteImage(imageUrl);
            if (!deleted) {
                return ResponseEntity.badRequest().body(ApiResponse.error("图片删除失败"));
            }

            // 4. 更新专业信息
            images.remove(imageUrl);
            majorInfo.setImages(images);
            majorInfoService.updateById(majorInfo);

            // 5. 返回结果
            Map<String, List<String>> result = new HashMap<>();
            result.put("images", images);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("专业图片删除失败: " + e.getMessage()));
        }
    }
    
    // ======== 学院图片上传接口 ========
    
    /**
     * 上传学院Logo
     */
    @PostMapping("/college/image/logo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "上传学院Logo", description = "上传并更新学院Logo，需要管理员权限")
    public ResponseEntity<?> uploadCollegeLogo(
            @Parameter(description = "学院ID", required = true) @PathVariable Long id,
            @Parameter(description = "Logo图片文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            // 1. 检查学院是否存在
            College college = collegeService.getById(id);
            if (college == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("学院不存在"));
            }

            // 2. 删除旧Logo
            if (college.getLogoUrl() != null) {
                fileUploadService.deleteImage(college.getLogoUrl());
            }

            // 3. 上传新Logo
            String logoUrl = fileUploadService.uploadImage(file, "college/logo");
            
            // 4. 更新学院信息
            college.setLogoUrl(logoUrl);
            collegeService.updateById(college);

            // 5. 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("logoUrl", logoUrl);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Logo上传失败: " + e.getMessage()));
        }
    }

    /**
     * 上传学院横幅
     */
    @PostMapping("/college/image/banner/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "上传学院横幅", description = "上传并更新学院横幅，需要管理员权限")
    public ResponseEntity<?> uploadCollegeBanner(
            @Parameter(description = "学院ID", required = true) @PathVariable Long id,
            @Parameter(description = "横幅图片文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            // 1. 检查学院是否存在
            College college = collegeService.getById(id);
            if (college == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("学院不存在"));
            }

            // 2. 删除旧横幅
            if (college.getBannerUrl() != null) {
                fileUploadService.deleteImage(college.getBannerUrl());
            }

            // 3. 上传新横幅
            String bannerUrl = fileUploadService.uploadImage(file, "college/banner");
            
            // 4. 更新学院信息
            college.setBannerUrl(bannerUrl);
            collegeService.updateById(college);

            // 5. 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("bannerUrl", bannerUrl);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("横幅上传失败: " + e.getMessage()));
        }
    }
} 