package com.zs.controller;

import com.zs.entity.SchoolInfo;
import com.zs.service.SchoolInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/school-info")
@Tag(name = "学校信息管理接口")
public class SchoolInfoController {

    @Autowired
    private SchoolInfoService schoolInfoService;

    @PostMapping("/create")
    @Operation(summary = "添加学校信息", description = "添加新的学校信息")
    public ResponseEntity<?> addSchoolInfo(@Parameter(description = "学校信息", required = true) @RequestBody SchoolInfo schoolInfo) {
        return schoolInfoService.save(schoolInfo) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学校信息", description = "根据学校ID删除学校信息")
    public ResponseEntity<?> deleteSchoolInfo(@Parameter(description = "学校ID", required = true) @PathVariable Long id) {
        return schoolInfoService.removeById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    @Operation(summary = "更新学校信息", description = "更新学校信息")
    public ResponseEntity<?> updateSchoolInfo(@Parameter(description = "学校信息", required = true) @RequestBody SchoolInfo schoolInfo) {
        return schoolInfoService.updateById(schoolInfo) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取学校信息", description = "根据学校ID获取学校信息")
    public ResponseEntity<SchoolInfo> getSchoolInfoById(@Parameter(description = "学校ID", required = true) @PathVariable Long id) {
        SchoolInfo schoolInfo = schoolInfoService.getById(id);
        return schoolInfo != null ? ResponseEntity.ok(schoolInfo) : ResponseEntity.notFound().build();
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有学校信息", description = "获取系统中所有学校信息")
    public ResponseEntity<List<SchoolInfo>> getAllSchoolInfos() {
        return ResponseEntity.ok(schoolInfoService.list());
    }

    @GetMapping("/search/name/{name}")
    @Operation(summary = "根据名称查询学校", description = "根据学校名称模糊查询学校信息")
    public ResponseEntity<List<SchoolInfo>> searchByName(@Parameter(description = "学校名称", required = true) @PathVariable String name) {
        return ResponseEntity.ok(schoolInfoService.searchByName(name));
    }

    @GetMapping("/search/province/{province}")
    @Operation(summary = "根据省份查询学校", description = "根据省份查询学校信息")
    public ResponseEntity<List<SchoolInfo>> getByProvince(@Parameter(description = "省份", required = true) @PathVariable String province) {
        return ResponseEntity.ok(schoolInfoService.getByProvince(province));
    }

    @GetMapping("/search/type/{type}")
    @Operation(summary = "根据类型查询学校", description = "根据学校类型查询学校信息")
    public ResponseEntity<List<SchoolInfo>> getByType(@Parameter(description = "学校类型", required = true) @PathVariable String type) {
        return ResponseEntity.ok(schoolInfoService.getByType(type));
    }

    @GetMapping("/search/province-city")
    @Operation(summary = "根据省份和城市查询学校", description = "根据省份和城市查询学校信息")
    public ResponseEntity<List<SchoolInfo>> getByProvinceAndCity(
            @Parameter(description = "省份", required = true) @RequestParam String province,
            @Parameter(description = "城市", required = true) @RequestParam String city) {
        return ResponseEntity.ok(schoolInfoService.getByProvinceAndCity(province, city));
    }

    @GetMapping("/search/level/{level}")
    @Operation(summary = "根据学校层次查询", description = "根据学校层次（985、211等）查询学校信息")
    public ResponseEntity<List<SchoolInfo>> getByLevel(@Parameter(description = "学校层次", required = true) @PathVariable String level) {
        return ResponseEntity.ok(schoolInfoService.getByLevel(level));
    }
}