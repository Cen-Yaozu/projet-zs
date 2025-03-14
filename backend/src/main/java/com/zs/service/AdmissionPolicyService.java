package com.zs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.entity.AdmissionPolicy;
import java.util.List;
import java.util.Map;

/**
 * 招生政策服务接口
 */
public interface AdmissionPolicyService extends IService<AdmissionPolicy> {
    /**
     * 根据学校ID查询招生政策
     */
    List<AdmissionPolicy> getBySchoolId(Long schoolId);

    /**
     * 根据年份和学校ID查询招生政策
     */
    List<AdmissionPolicy> getByYearAndSchoolId(Integer year, Long schoolId);

    /**
     * 根据省份查询招生政策
     */
    List<AdmissionPolicy> getByProvince(String province);

    /**
     * 根据年份、学校ID和省份查询招生政策
     */
    List<AdmissionPolicy> getByYearAndSchoolIdAndProvince(Integer year, Long schoolId, String province);

    /**
     * 根据分数范围查询招生政策
     */
    List<AdmissionPolicy> getByScoreRange(Integer minScore, Integer maxScore);
    
    /**
     * 查询专业分数线数据
     * 返回包含专业名称、最高分、最低分、省控线等信息的数据
     */
    List<Map<String, Object>> getMajorScores(Integer year, Long schoolId, String province);
}