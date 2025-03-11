package com.zs.controller;

import com.zs.entity.AdmissionPolicy;
import com.zs.service.AdmissionPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admission-policy")
@Tag(name = "招生政策管理接口")
public class AdmissionPolicyController {

    @Autowired
    private AdmissionPolicyService admissionPolicyService;

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
    @Operation(summary = "获取所有招生政策", description = "获取系统中所有招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getAllAdmissionPolicies() {
        return ResponseEntity.ok(admissionPolicyService.list());
    }

    @GetMapping("/school/{schoolId}")
    @Operation(summary = "根据学校ID查询招生政策", description = "查询指定学校的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getBySchoolId(@Parameter(description = "学校ID", required = true) @PathVariable Long schoolId) {
        return ResponseEntity.ok(admissionPolicyService.getBySchoolId(schoolId));
    }

    @GetMapping("/search/year-school")
    @Operation(summary = "根据年份和学校ID查询招生政策", description = "查询指定年份和学校的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getByYearAndSchoolId(
            @Parameter(description = "年份", required = true) @RequestParam Integer year,
            @Parameter(description = "学校ID", required = true) @RequestParam Long schoolId) {
        return ResponseEntity.ok(admissionPolicyService.getByYearAndSchoolId(year, schoolId));
    }

    @GetMapping("/search/province/{province}")
    @Operation(summary = "根据省份查询招生政策", description = "查询指定省份的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getByProvince(@Parameter(description = "省份", required = true) @PathVariable String province) {
        return ResponseEntity.ok(admissionPolicyService.getByProvince(province));
    }

    @GetMapping("/search/year-school-province")
    @Operation(summary = "根据年份、学校ID和省份查询招生政策", description = "查询指定年份、学校和省份的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getByYearAndSchoolIdAndProvince(
            @Parameter(description = "年份", required = true) @RequestParam Integer year,
            @Parameter(description = "学校ID", required = true) @RequestParam Long schoolId,
            @Parameter(description = "省份", required = true) @RequestParam String province) {
        return ResponseEntity.ok(admissionPolicyService.getByYearAndSchoolIdAndProvince(year, schoolId, province));
    }

    @GetMapping("/search/score-range")
    @Operation(summary = "根据分数范围查询招生政策", description = "查询指定分数范围内的招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getByScoreRange(
            @Parameter(description = "最低分数", required = true) @RequestParam Integer minScore,
            @Parameter(description = "最高分数", required = true) @RequestParam Integer maxScore) {
        return ResponseEntity.ok(admissionPolicyService.getByScoreRange(minScore, maxScore));
    }
}