package com.zs.service.impl;

import com.zs.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务实现类
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.path}")
    private String fileUploadPath;
    
    /**
     * 上传图片文件
     * @param file 上传的图片文件
     * @param directory 保存目录名称(相对于上传根目录)
     * @return 图片访问URL
     * @throws IOException 如果上传过程中发生IO异常
     */
    @Override
    public String uploadImage(MultipartFile file, String directory) throws IOException {
        // 1. 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只允许上传图片文件");
        }

        // 2. 创建上传目录
        String uploadDir = fileUploadPath + "/" + directory + "/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 3. 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // 4. 保存文件
        Path filePath = Paths.get(uploadDir + filename);
        Files.copy(file.getInputStream(), filePath);

        // 5. 返回访问URL
        return "/static/" + directory + "/" + filename;
    }

    /**
     * 上传多个图片文件
     * @param files 上传的多个图片文件
     * @param directory 保存目录名称(相对于上传根目录)
     * @return 多个图片访问URL列表
     * @throws IOException 如果上传过程中发生IO异常
     */
    @Override
    public String[] uploadMultipleImages(MultipartFile[] files, String directory) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadImage(file, directory);
            urls.add(url);
        }
        return urls.toArray(new String[0]);
    }
} 