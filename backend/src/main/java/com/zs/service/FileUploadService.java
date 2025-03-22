package com.zs.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {
    
    /**
     * 上传图片文件
     * @param file 上传的图片文件
     * @param directory 保存目录名称(相对于上传根目录)
     * @return 图片访问URL
     * @throws IOException 如果上传过程中发生IO异常
     */
    String uploadImage(MultipartFile file, String directory) throws IOException;
    
    /**
     * 上传多个图片文件
     * @param files 上传的多个图片文件
     * @param directory 保存目录名称(相对于上传根目录) 
     * @return 多个图片访问URL列表
     * @throws IOException 如果上传过程中发生IO异常
     */
    String[] uploadMultipleImages(MultipartFile[] files, String directory) throws IOException;
} 