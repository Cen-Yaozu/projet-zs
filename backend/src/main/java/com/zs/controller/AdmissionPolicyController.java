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

    @PostMapping
    @Operation(summary = "添加招生政策", description = "添加新的招生政策信息")
    public ResponseEntity<?> addAdmissionPolicy(@Parameter(description = "招生政策信息", required = true) @RequestBody AdmissionPolicy admissionPolicy) {
        boolean success = admissionPolicyService.addAdmissionPolicy(admissionPolicy);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除招生政策", description = "根据招生政策ID删除招生政策")
    public ResponseEntity<?> deleteAdmissionPolicy(@Parameter(description = "招生政策ID", required = true) @PathVariable Long id) {
        boolean success = admissionPolicyService.deleteAdmissionPolicy(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    @Operation(summary = "更新招生政策", description = "更新招生政策信息")
    public ResponseEntity<?> updateAdmissionPolicy(@Parameter(description = "招生政策信息", required = true) @RequestBody AdmissionPolicy admissionPolicy) {
        boolean success = admissionPolicyService.updateAdmissionPolicy(admissionPolicy);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取招生政策", description = "根据招生政策ID获取招生政策信息")
    public ResponseEntity<AdmissionPolicy> getAdmissionPolicyById(@Parameter(description = "招生政策ID", required = true) @PathVariable Long id) {
        AdmissionPolicy policy = admissionPolicyService.getAdmissionPolicyById(id);
        return policy != null ? ResponseEntity.ok(policy) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "获取所有招生政策", description = "获取系统中所有招生政策信息")
    public ResponseEntity<List<AdmissionPolicy>> getAllAdmissionPolicies() {
        List<AdmissionPolicy> policies = admissionPolicyService.getAllAdmissionPolicies();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/school/{schoolId}")
    @Operation(summary = "根据学校ID查询招生政策", description = "根据学校ID查询相关招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getAdmissionPoliciesBySchoolId(@Parameter(description = "学校ID", required = true) @PathVariable Long schoolId) {
        List<AdmissionPolicy> policies = admissionPolicyService.getAdmissionPoliciesBySchoolId(schoolId);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/school/{schoolId}/year/{year}")
    @Operation(summary = "根据年份和学校ID查询招生政策", description = "根据年份和学校ID查询相关招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getAdmissionPoliciesByYearAndSchoolId(
            @Parameter(description = "年份", required = true) @PathVariable Integer year,
            @Parameter(description = "学校ID", required = true) @PathVariable Long schoolId) {
        List<AdmissionPolicy> policies = admissionPolicyService.getAdmissionPoliciesByYearAndSchoolId(year, schoolId);
        return ResponseEntity.ok(policies);
    }
}