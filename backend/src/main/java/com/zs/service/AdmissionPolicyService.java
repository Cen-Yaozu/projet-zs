package com.zs.service;

import com.zs.entity.AdmissionPolicy;
import java.util.List;

public interface AdmissionPolicyService {
    // 添加招生政策
    boolean addAdmissionPolicy(AdmissionPolicy admissionPolicy);

    // 删除招生政策
    boolean deleteAdmissionPolicy(Long id);

    // 更新招生政策
    boolean updateAdmissionPolicy(AdmissionPolicy admissionPolicy);

    // 根据ID获取招生政策
    AdmissionPolicy getAdmissionPolicyById(Long id);

    // 获取所有招生政策
    List<AdmissionPolicy> getAllAdmissionPolicies();

    // 根据学校ID获取招生政策
    List<AdmissionPolicy> getAdmissionPoliciesBySchoolId(Long schoolId);

    // 根据年份和学校ID获取招生政策
    List<AdmissionPolicy> getAdmissionPoliciesByYearAndSchoolId(Integer year, Long schoolId);
}