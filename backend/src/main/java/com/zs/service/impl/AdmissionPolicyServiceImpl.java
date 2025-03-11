package com.zs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.AdmissionPolicy;
import com.zs.mapper.AdmissionPolicyMapper;
import com.zs.service.AdmissionPolicyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmissionPolicyServiceImpl extends ServiceImpl<AdmissionPolicyMapper, AdmissionPolicy> implements AdmissionPolicyService {

    @Override
    public List<AdmissionPolicy> getBySchoolId(Long schoolId) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getSchoolId, schoolId));
    }

    @Override
    public List<AdmissionPolicy> getByYearAndSchoolId(Integer year, Long schoolId) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getYear, year)
                .eq(AdmissionPolicy::getSchoolId, schoolId));
    }

    @Override
    public List<AdmissionPolicy> getByProvince(String province) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getProvince, province));
    }

    @Override
    public List<AdmissionPolicy> getByYearAndSchoolIdAndProvince(Integer year, Long schoolId, String province) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getYear, year)
                .eq(AdmissionPolicy::getSchoolId, schoolId)
                .eq(AdmissionPolicy::getProvince, province));
    }

    @Override
    public List<AdmissionPolicy> getByScoreRange(Integer minScore, Integer maxScore) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .ge(AdmissionPolicy::getMinScore, minScore)
                .le(AdmissionPolicy::getMinScore, maxScore));
    }
}