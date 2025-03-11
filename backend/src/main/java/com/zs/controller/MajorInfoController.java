package com.zs.controller;

import com.zs.entity.MajorInfo;
import com.zs.service.MajorInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/major-info")
@Tag(name = "专业信息管理接口")
public class MajorInfoController {

    @Autowired
    private MajorInfoService majorInfoService;

    @PostMapping
    @Operation(summary = "添加专业信息", description = "添加新的专业信息")
    public ResponseEntity<?> addMajorInfo(@Parameter(description = "专业信息", required = true) @RequestBody MajorInfo majorInfo) {
        boolean success = majorInfoService.addMajorInfo(majorInfo);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专业信息", description = "根据专业ID删除专业信息")
    public ResponseEntity<?> deleteMajorInfo(@Parameter(description = "专业ID", required = true) @PathVariable Long id) {
        boolean success = majorInfoService.deleteMajorInfo(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    @Operation(summary = "更新专业信息", description = "更新专业信息")
    public ResponseEntity<?> updateMajorInfo(@Parameter(description = "专业信息", required = true) @RequestBody MajorInfo majorInfo) {
        boolean success = majorInfoService.updateMajorInfo(majorInfo);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取专业信息", description = "根据专业ID获取专业信息")
    public ResponseEntity<MajorInfo> getMajorInfoById(@Parameter(description = "专业ID", required = true) @PathVariable Long id) {
        MajorInfo majorInfo = majorInfoService.getMajorInfoById(id);
        return majorInfo != null ? ResponseEntity.ok(majorInfo) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "获取所有专业信息", description = "获取系统中所有专业信息")
    public ResponseEntity<List<MajorInfo>> getAllMajorInfos() {
        List<MajorInfo> majorInfos = majorInfoService.getAllMajorInfos();
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/school/{schoolId}")
    @Operation(summary = "根据学校ID查询专业", description = "根据学校ID查询相关专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosBySchoolId(@Parameter(description = "学校ID", required = true) @PathVariable Long schoolId) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosBySchoolId(schoolId);
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/search/name/{name}")
    @Operation(summary = "根据名称查询专业", description = "根据专业名称查询专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosByName(@Parameter(description = "专业名称", required = true) @PathVariable String name) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosByName(name);
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/search/type/{type}")
    @Operation(summary = "根据类型查询专业", description = "根据专业类型查询专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosByType(@Parameter(description = "专业类型", required = true) @PathVariable String type) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosByType(type);
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/search/category/{category}")
    @Operation(summary = "根据类别查询专业", description = "根据专业类别查询专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosByCategory(@Parameter(description = "专业类别", required = true) @PathVariable String category) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosByCategory(category);
        return ResponseEntity.ok(majorInfos);
    }
}