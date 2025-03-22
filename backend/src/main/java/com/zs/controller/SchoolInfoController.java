package com.zs.controller;

import com.zs.entity.SchoolInfo;
import com.zs.service.SchoolInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/school-info")
@Tag(name = "学校信息接口")
@PermitAll
public class SchoolInfoController {

    @Autowired
    private SchoolInfoService schoolInfoService;
    
    // 广州航海学院ID，固定为1
    private static final Long SCHOOL_ID = 1L;

    @GetMapping("/info")
    @Operation(summary = "获取学校信息", description = "获取广州航海学院的基本信息")
    public ResponseEntity<SchoolInfo> getSchoolInfo() {
        // 创建一个固定的广州航海学院对象，不从数据库查询
        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setId(SCHOOL_ID);
        schoolInfo.setName("广州航海学院");
        schoolInfo.setCode("12573");
        schoolInfo.setType("本科");
        schoolInfo.setLevel("普通高等学校");
        schoolInfo.setProvince("广东省");
        schoolInfo.setCity("广州市");
        schoolInfo.setAddress("广东省广州市黄埔区红山大道8号");
        schoolInfo.setDescription("广州航海学院（Guangzhou Maritime University）是广东省人民政府举办的全日制普通本科高等学校。");
        schoolInfo.setWebsite("https://www.gzhmu.edu.cn");
        schoolInfo.setCreateTime(LocalDateTime.now());
        schoolInfo.setUpdateTime(LocalDateTime.now());
        
        return ResponseEntity.ok(schoolInfo);
    }
}