package com.zs.controller;

import com.zs.entity.SchoolInfo;
import com.zs.service.SchoolInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/school-info")
@Api(tags = "学校信息管理接口")
public class SchoolInfoController {

    @Autowired
    private SchoolInfoService schoolInfoService;

    @PostMapping
    @ApiOperation(value = "添加学校信息", notes = "添加新的学校信息")
    public ResponseEntity<?> addSchoolInfo(@ApiParam(value = "学校信息", required = true) @RequestBody SchoolInfo schoolInfo) {
        boolean success = schoolInfoService.addSchoolInfo(schoolInfo);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除学校信息", notes = "根据学校ID删除学校信息")
    public ResponseEntity<?> deleteSchoolInfo(@ApiParam(value = "学校ID", required = true) @PathVariable Long id) {
        boolean success = schoolInfoService.deleteSchoolInfo(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    @ApiOperation(value = "更新学校信息", notes = "更新学校信息")
    public ResponseEntity<?> updateSchoolInfo(@ApiParam(value = "学校信息", required = true) @RequestBody SchoolInfo schoolInfo) {
        boolean success = schoolInfoService.updateSchoolInfo(schoolInfo);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取学校信息", notes = "根据学校ID获取学校信息")
    public ResponseEntity<SchoolInfo> getSchoolInfoById(@ApiParam(value = "学校ID", required = true) @PathVariable Long id) {
        SchoolInfo schoolInfo = schoolInfoService.getSchoolInfoById(id);
        return schoolInfo != null ? ResponseEntity.ok(schoolInfo) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @ApiOperation(value = "获取所有学校信息", notes = "获取系统中所有学校信息")
    public ResponseEntity<List<SchoolInfo>> getAllSchoolInfos() {
        List<SchoolInfo> schoolInfos = schoolInfoService.getAllSchoolInfos();
        return ResponseEntity.ok(schoolInfos);
    }

    @GetMapping("/search/name/{name}")
    @ApiOperation(value = "根据名称查询学校", notes = "根据学校名称查询学校信息")
    public ResponseEntity<List<SchoolInfo>> getSchoolInfosByName(@ApiParam(value = "学校名称", required = true) @PathVariable String name) {
        List<SchoolInfo> schoolInfos = schoolInfoService.getSchoolInfosByName(name);
        return ResponseEntity.ok(schoolInfos);
    }

    @GetMapping("/search/province/{province}")
    @ApiOperation(value = "根据省份查询学校", notes = "根据省份查询学校信息")
    public ResponseEntity<List<SchoolInfo>> getSchoolInfosByProvince(@ApiParam(value = "省份", required = true) @PathVariable String province) {
        List<SchoolInfo> schoolInfos = schoolInfoService.getSchoolInfosByProvince(province);
        return ResponseEntity.ok(schoolInfos);
    }

    @GetMapping("/search/type/{type}")
    @ApiOperation(value = "根据类型查询学校", notes = "根据学校类型查询学校信息")
    public ResponseEntity<List<SchoolInfo>> getSchoolInfosByType(@ApiParam(value = "学校类型", required = true) @PathVariable String type) {
        List<SchoolInfo> schoolInfos = schoolInfoService.getSchoolInfosByType(type);
        return ResponseEntity.ok(schoolInfos);
    }
}