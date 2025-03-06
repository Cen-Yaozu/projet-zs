package com.zs.controller;

import com.zs.entity.MajorInfo;
import com.zs.service.MajorInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/major-info")
@Api(tags = "专业信息管理接口")
public class MajorInfoController {

    @Autowired
    private MajorInfoService majorInfoService;

    @PostMapping
    @ApiOperation(value = "添加专业信息", notes = "添加新的专业信息")
    public ResponseEntity<?> addMajorInfo(@ApiParam(value = "专业信息", required = true) @RequestBody MajorInfo majorInfo) {
        boolean success = majorInfoService.addMajorInfo(majorInfo);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除专业信息", notes = "根据专业ID删除专业信息")
    public ResponseEntity<?> deleteMajorInfo(@ApiParam(value = "专业ID", required = true) @PathVariable Long id) {
        boolean success = majorInfoService.deleteMajorInfo(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    @ApiOperation(value = "更新专业信息", notes = "更新专业信息")
    public ResponseEntity<?> updateMajorInfo(@ApiParam(value = "专业信息", required = true) @RequestBody MajorInfo majorInfo) {
        boolean success = majorInfoService.updateMajorInfo(majorInfo);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取专业信息", notes = "根据专业ID获取专业信息")
    public ResponseEntity<MajorInfo> getMajorInfoById(@ApiParam(value = "专业ID", required = true) @PathVariable Long id) {
        MajorInfo majorInfo = majorInfoService.getMajorInfoById(id);
        return majorInfo != null ? ResponseEntity.ok(majorInfo) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @ApiOperation(value = "获取所有专业信息", notes = "获取系统中所有专业信息")
    public ResponseEntity<List<MajorInfo>> getAllMajorInfos() {
        List<MajorInfo> majorInfos = majorInfoService.getAllMajorInfos();
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/school/{schoolId}")
    @ApiOperation(value = "根据学校ID查询专业", notes = "根据学校ID查询相关专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosBySchoolId(@ApiParam(value = "学校ID", required = true) @PathVariable Long schoolId) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosBySchoolId(schoolId);
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/search/name/{name}")
    @ApiOperation(value = "根据名称查询专业", notes = "根据专业名称查询专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosByName(@ApiParam(value = "专业名称", required = true) @PathVariable String name) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosByName(name);
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/search/type/{type}")
    @ApiOperation(value = "根据类型查询专业", notes = "根据专业类型查询专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosByType(@ApiParam(value = "专业类型", required = true) @PathVariable String type) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosByType(type);
        return ResponseEntity.ok(majorInfos);
    }

    @GetMapping("/search/category/{category}")
    @ApiOperation(value = "根据类别查询专业", notes = "根据专业类别查询专业信息")
    public ResponseEntity<List<MajorInfo>> getMajorInfosByCategory(@ApiParam(value = "专业类别", required = true) @PathVariable String category) {
        List<MajorInfo> majorInfos = majorInfoService.getMajorInfosByCategory(category);
        return ResponseEntity.ok(majorInfos);
    }
}