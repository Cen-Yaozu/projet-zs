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

    @PostMapping("/create")
    @Operation(summary = "添加专业信息", description = "添加新的专业信息")
    public ResponseEntity<?> addMajorInfo(@Parameter(description = "专业信息", required = true) @RequestBody MajorInfo majorInfo) {
        return majorInfoService.save(majorInfo) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专业信息", description = "根据专业ID删除专业信息")
    public ResponseEntity<?> deleteMajorInfo(@Parameter(description = "专业ID", required = true) @PathVariable Long id) {
        return majorInfoService.removeById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    @Operation(summary = "更新专业信息", description = "更新专业信息")
    public ResponseEntity<?> updateMajorInfo(@Parameter(description = "专业信息", required = true) @RequestBody MajorInfo majorInfo) {
        return majorInfoService.updateById(majorInfo) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取专业信息", description = "根据专业ID获取专业详细信息")
    public ResponseEntity<MajorInfo> getMajorInfoById(@Parameter(description = "专业ID", required = true) @PathVariable Long id) {
        MajorInfo majorInfo = majorInfoService.getById(id);
        return majorInfo != null ? ResponseEntity.ok(majorInfo) : ResponseEntity.notFound().build();
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有专业信息", description = "获取系统中所有专业信息")
    public ResponseEntity<List<MajorInfo>> getAllMajorInfos() {
        return ResponseEntity.ok(majorInfoService.list());
    }

    @GetMapping("/school/{schoolId}")
    @Operation(summary = "根据学校ID查询专业", description = "根据学校ID查询相关专业信息")
    public ResponseEntity<List<MajorInfo>> getBySchoolId(@Parameter(description = "学校ID", required = true) @PathVariable Long schoolId) {
        return ResponseEntity.ok(majorInfoService.getBySchoolId(schoolId));
    }
    
    @GetMapping("/college/{collegeId}")
    @Operation(summary = "根据院系ID查询专业", description = "根据院系ID查询相关专业信息")
    public ResponseEntity<List<MajorInfo>> getByCollegeId(@Parameter(description = "院系ID", required = true) @PathVariable Long collegeId) {
        return ResponseEntity.ok(majorInfoService.getByCollegeId(collegeId));
    }

    @GetMapping("/search/name/{name}")
    @Operation(summary = "根据名称查询专业", description = "根据专业名称模糊查询专业信息")
    public ResponseEntity<List<MajorInfo>> searchByName(@Parameter(description = "专业名称", required = true) @PathVariable String name) {
        return ResponseEntity.ok(majorInfoService.searchByName(name));
    }

    @GetMapping("/search/degree/{degree}")
    @Operation(summary = "根据学位类型查询专业", description = "根据学位类型查询专业信息")
    public ResponseEntity<List<MajorInfo>> getByDegree(@Parameter(description = "学位类型", required = true) @PathVariable String degree) {
        return ResponseEntity.ok(majorInfoService.getByDegree(degree));
    }
}