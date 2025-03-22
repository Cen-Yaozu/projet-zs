package com.zs.controller;

import com.zs.entity.MajorScore;
import com.zs.service.MajorScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/major-score")
@Tag(name = "专业分数管理接口")
@PermitAll
public class MajorScoreController {

    @Autowired
    private MajorScoreService majorScoreService;

    @PostMapping("/create")
    @Operation(summary = "添加专业分数", description = "添加新的专业分数记录")
    public ResponseEntity<?> addMajorScore(@Parameter(description = "专业分数", required = true) @RequestBody MajorScore majorScore) {
        return majorScoreService.save(majorScore) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专业分数", description = "根据ID删除专业分数")
    public ResponseEntity<?> deleteMajorScore(@Parameter(description = "分数ID", required = true) @PathVariable Long id) {
        return majorScoreService.removeById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    @Operation(summary = "更新专业分数", description = "更新专业分数记录")
    public ResponseEntity<?> updateMajorScore(@Parameter(description = "专业分数", required = true) @RequestBody MajorScore majorScore) {
        return majorScoreService.updateById(majorScore) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取专业分数", description = "根据ID获取专业分数")
    public ResponseEntity<MajorScore> getMajorScoreById(@Parameter(description = "分数ID", required = true) @PathVariable Long id) {
        MajorScore score = majorScoreService.getById(id);
        return score != null ? ResponseEntity.ok(score) : ResponseEntity.notFound().build();
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有专业分数", description = "获取所有专业分数记录")
    public ResponseEntity<List<MajorScore>> getAllMajorScores() {
        return ResponseEntity.ok(majorScoreService.list());
    }

    @GetMapping("/search/year")
    @Operation(summary = "根据年份查询专业分数", description = "查询指定年份的专业分数")
    public ResponseEntity<List<MajorScore>> getByYear(
            @Parameter(description = "年份", required = true) @RequestParam Integer year) {
        List<MajorScore> scores = majorScoreService.list().stream()
            .filter(score -> year.equals(score.getYear()))
            .toList();
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/search/province/{province}")
    @Operation(summary = "根据省份查询专业分数", description = "查询指定省份的专业分数")
    public ResponseEntity<List<MajorScore>> getByProvince(@Parameter(description = "省份", required = true) @PathVariable String province) {
        List<MajorScore> scores = majorScoreService.getByProvince(province);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/search/year-province")
    @Operation(summary = "根据年份和省份查询专业分数", description = "查询指定年份和省份的专业分数")
    public ResponseEntity<List<MajorScore>> getByYearAndProvince(
            @Parameter(description = "年份", required = true) @RequestParam Integer year,
            @Parameter(description = "省份", required = true) @RequestParam String province) {
        List<MajorScore> scores = majorScoreService.getByProvince(province).stream()
            .filter(score -> year.equals(score.getYear()))
            .toList();
        return ResponseEntity.ok(scores);
    }
    
    @GetMapping("/search/major/{majorName}")
    @Operation(summary = "根据专业名称查询专业分数", description = "查询指定专业名称的分数记录")
    public ResponseEntity<List<MajorScore>> getByMajorName(@Parameter(description = "专业名称", required = true) @PathVariable String majorName) {
        List<MajorScore> scores = majorScoreService.getByMajorName(majorName);
        return ResponseEntity.ok(scores);
    }
    
    @GetMapping("/search/year-major")
    @Operation(summary = "根据年份和专业名称查询专业分数", description = "查询指定年份和专业的分数记录")
    public ResponseEntity<List<MajorScore>> getByYearAndMajorName(
            @Parameter(description = "年份", required = true) @RequestParam Integer year,
            @Parameter(description = "专业名称", required = true) @RequestParam String majorName) {
        List<MajorScore> scores = majorScoreService.getByMajorName(majorName).stream()
            .filter(score -> year.equals(score.getYear()))
            .toList();
        return ResponseEntity.ok(scores);
    }
    
    @PostMapping("/fill-data")
    @Operation(summary = "填充历史分数数据", description = "从招生政策数据中提取并填充专业分数数据")
    public ResponseEntity<?> fillHistoricalScoreData() {
        return majorScoreService.fillHistoricalScoreData() ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
} 