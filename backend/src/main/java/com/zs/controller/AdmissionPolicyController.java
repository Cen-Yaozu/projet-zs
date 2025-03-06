package com.zs.controller;

import com.zs.entity.AdmissionPolicy;
import com.zs.service.AdmissionPolicyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admission-policy")
@Api(tags = "招生政策管理接口")
public class AdmissionPolicyController {

    @Autowired
    private AdmissionPolicyService admissionPolicyService;

    @PostMapping
    @ApiOperation(value = "添加招生政策", notes = "添加新的招生政策信息")
    public ResponseEntity<?> addAdmissionPolicy(@ApiParam(value = "招生政策信息", required = true) @RequestBody AdmissionPolicy admissionPolicy) {
        boolean success = admissionPolicyService.addAdmissionPolicy(admissionPolicy);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除招生政策", notes = "根据招生政策ID删除招生政策")
    public ResponseEntity<?> deleteAdmissionPolicy(@ApiParam(value = "招生政策ID", required = true) @PathVariable Long id) {
        boolean success = admissionPolicyService.deleteAdmissionPolicy(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    @ApiOperation(value = "更新招生政策", notes = "更新招生政策信息")
    public ResponseEntity<?> updateAdmissionPolicy(@ApiParam(value = "招生政策信息", required = true) @RequestBody AdmissionPolicy admissionPolicy) {
        boolean success = admissionPolicyService.updateAdmissionPolicy(admissionPolicy);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取招生政策", notes = "根据招生政策ID获取招生政策信息")
    public ResponseEntity<AdmissionPolicy> getAdmissionPolicyById(@ApiParam(value = "招生政策ID", required = true) @PathVariable Long id) {
        AdmissionPolicy policy = admissionPolicyService.getAdmissionPolicyById(id);
        return policy != null ? ResponseEntity.ok(policy) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @ApiOperation(value = "获取所有招生政策", notes = "获取系统中所有招生政策信息")
    public ResponseEntity<List<AdmissionPolicy>> getAllAdmissionPolicies() {
        List<AdmissionPolicy> policies = admissionPolicyService.getAllAdmissionPolicies();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/school/{schoolId}")
    @ApiOperation(value = "根据学校ID查询招生政策", notes = "根据学校ID查询相关招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getAdmissionPoliciesBySchoolId(@ApiParam(value = "学校ID", required = true) @PathVariable Long schoolId) {
        List<AdmissionPolicy> policies = admissionPolicyService.getAdmissionPoliciesBySchoolId(schoolId);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/school/{schoolId}/year/{year}")
    @ApiOperation(value = "根据年份和学校ID查询招生政策", notes = "根据年份和学校ID查询相关招生政策")
    public ResponseEntity<List<AdmissionPolicy>> getAdmissionPoliciesByYearAndSchoolId(
            @ApiParam(value = "年份", required = true) @PathVariable Integer year,
            @ApiParam(value = "学校ID", required = true) @PathVariable Long schoolId) {
        List<AdmissionPolicy> policies = admissionPolicyService.getAdmissionPoliciesByYearAndSchoolId(year, schoolId);
        return ResponseEntity.ok(policies);
    }
}