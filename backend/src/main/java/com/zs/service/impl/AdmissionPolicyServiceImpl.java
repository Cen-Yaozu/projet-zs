package com.zs.service.impl;

import com.zs.entity.AdmissionPolicy;
import com.zs.mapper.AdmissionPolicyMapper;
import com.zs.service.AdmissionPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdmissionPolicyServiceImpl implements AdmissionPolicyService {

    @Autowired
    private AdmissionPolicyMapper admissionPolicyMapper;

    @Override
    public boolean addAdmissionPolicy(AdmissionPolicy admissionPolicy) {
        return admissionPolicyMapper.insert(admissionPolicy) > 0;
    }

    @Override
    public boolean deleteAdmissionPolicy(Long id) {
        return admissionPolicyMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateAdmissionPolicy(AdmissionPolicy admissionPolicy) {
        return admissionPolicyMapper.update(admissionPolicy) > 0;
    }

    @Override
    public AdmissionPolicy getAdmissionPolicyById(Long id) {
        return admissionPolicyMapper.selectById(id);
    }

    @Override
    public List<AdmissionPolicy> getAllAdmissionPolicies() {
        return admissionPolicyMapper.selectAll();
    }

    @Override
    public List<AdmissionPolicy> getAdmissionPoliciesBySchoolId(Long schoolId) {
        return admissionPolicyMapper.selectBySchoolId(schoolId);
    }

    @Override
    public List<AdmissionPolicy> getAdmissionPoliciesByYearAndSchoolId(Integer year, Long schoolId) {
        return admissionPolicyMapper.selectByYearAndSchoolId(year, schoolId);
    }
}