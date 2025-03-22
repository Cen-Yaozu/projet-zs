package com.zs.controller;

import com.zs.entity.AdmissionPolicy;
import com.zs.entity.MajorScore;
import com.zs.service.AdmissionPolicyService;
import com.zs.service.MajorScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admission-policy")
@Tag(name = "招生政策管理接口")
@PermitAll
public class AdmissionPolicyController {

    @Autowired
    private AdmissionPolicyService admissionPolicyService;
    
    @Autowired
    private MajorScoreService majorScoreService;

    @PostMapping("/create")
    @Operation(summary = "添加招生政策", description = "添加新的招生政策")
    public ResponseEntity<?> addAdmissionPolicy(@Parameter(description = "招生政策", required = true) @RequestBody AdmissionPolicy admissionPolicy) {
        return admissionPolicyService.save(admissionPolicy) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除招生政策", description = "根据ID删除招生政策")
    public ResponseEntity<?> deleteAdmissionPolicy(@Parameter(description = "政策ID", required = true) @PathVariable Long id) {
        return admissionPolicyService.removeById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    @Operation(summary = "更新招生政策", description = "更新招生政策")
    public ResponseEntity<?> updateAdmissionPolicy(@Parameter(description = "招生政策", required = true) @RequestBody AdmissionPolicy admissionPolicy) {
        return admissionPolicyService.updateById(admissionPolicy) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取招生政策", description = "根据ID获取招生政策")
    public ResponseEntity<AdmissionPolicy> getAdmissionPolicyById(@Parameter(description = "政策ID", required = true) @PathVariable Long id) {
        AdmissionPolicy policy = admissionPolicyService.getById(id);
        return policy != null ? ResponseEntity.ok(policy) : ResponseEntity.notFound().build();
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有招生政策", description = "获取所有招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getAllAdmissionPolicies() {
        return ResponseEntity.ok(admissionPolicyService.list());
    }

    @GetMapping("/years")
    @Operation(summary = "获取所有年份", description = "获取所有招生年份")
    public ResponseEntity<List<AdmissionPolicy>> getAvailableYears() {
        return ResponseEntity.ok(admissionPolicyService.list());
    }

    @GetMapping("/search/year")
    @Operation(summary = "根据年份查询招生政策", description = "查询指定年份的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getByYear(
            @Parameter(description = "年份", required = true) @RequestParam Integer year) {
        List<AdmissionPolicy> policies = admissionPolicyService.list().stream()
                .filter(policy -> year.equals(policy.getYear()))
                .toList();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/search/province/{province}")
    @Operation(summary = "根据省份查询招生政策", description = "查询指定省份的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getByProvince(@Parameter(description = "省份", required = true) @PathVariable String province) {
        return ResponseEntity.ok(admissionPolicyService.getByProvince(province));
    }

    @GetMapping("/search/year-province")
    @Operation(summary = "根据年份和省份查询招生政策", description = "查询指定年份和省份的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getByYearAndProvince(
            @Parameter(description = "年份", required = true) @RequestParam Integer year,
            @Parameter(description = "省份", required = true) @RequestParam String province) {
        List<AdmissionPolicy> policies = admissionPolicyService.getByProvince(province).stream()
                .filter(policy -> year.equals(policy.getYear()))
                .toList();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/search/major-scores")
    @Operation(summary = "查询专业分数线数据", description = "查询指定年份的专业分数线详细数据")
    public ResponseEntity<List<MajorScore>> getMajorScores(
            @Parameter(description = "年份", required = true) @RequestParam Integer year,
            @Parameter(description = "省份", required = false) @RequestParam(required = false) String province) {
        if (province != null && !province.isEmpty()) {
            List<MajorScore> scores = majorScoreService.list().stream()
                    .filter(score -> year.equals(score.getYear()) && province.equals(score.getProvince()))
                    .toList();
            return ResponseEntity.ok(scores);
        } else {
            List<MajorScore> scores = majorScoreService.list().stream()
                    .filter(score -> year.equals(score.getYear()))
                    .toList();
            return ResponseEntity.ok(scores);
        }
    }
}