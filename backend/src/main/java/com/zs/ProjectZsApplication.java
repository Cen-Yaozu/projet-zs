package com.zs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.zs.mapper")
public class ProjectZsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectZsApplication.class, args);
    }
}