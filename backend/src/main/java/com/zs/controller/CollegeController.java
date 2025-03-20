package com.zs.controller;

import com.zs.entity.College;
import com.zs.service.CollegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/college")
@Tag(name = "院系信息管理接口")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @PostMapping("/create")
    @Operation(summary = "添加院系信息", description = "添加新的院系信息")
    public ResponseEntity<?> addCollege(@Parameter(description = "院系信息", required = true) @RequestBody College college) {
        return collegeService.save(college) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除院系信息", description = "根据院系ID删除院系信息")
    public ResponseEntity<?> deleteCollege(@Parameter(description = "院系ID", required = true) @PathVariable Long id) {
        return collegeService.removeById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    @Operation(summary = "更新院系信息", description = "更新院系信息")
    public ResponseEntity<?> updateCollege(@Parameter(description = "院系信息", required = true) @RequestBody College college) {
        return collegeService.updateById(college) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取院系信息", description = "根据院系ID获取院系信息")
    public ResponseEntity<College> getCollegeById(@Parameter(description = "院系ID", required = true) @PathVariable Long id) {
        College college = collegeService.getById(id);
        return college != null ? ResponseEntity.ok(college) : ResponseEntity.notFound().build();
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有院系信息", description = "获取系统中所有院系信息")
    public ResponseEntity<List<College>> getAllColleges() {
        return ResponseEntity.ok(collegeService.list());
    }

    @GetMapping("/school/{schoolId}")
    @Operation(summary = "根据学校ID查询院系", description = "根据学校ID查询相关院系信息")
    public ResponseEntity<List<College>> getBySchoolId(@Parameter(description = "学校ID", required = true) @PathVariable Long schoolId) {
        return ResponseEntity.ok(collegeService.getBySchoolId(schoolId));
    }

    @GetMapping("/search/name/{name}")
    @Operation(summary = "根据名称查询院系", description = "根据院系名称模糊查询院系信息")
    public ResponseEntity<List<College>> searchByName(@Parameter(description = "院系名称", required = true) @PathVariable String name) {
        return ResponseEntity.ok(collegeService.searchByName(name));
    }
} 