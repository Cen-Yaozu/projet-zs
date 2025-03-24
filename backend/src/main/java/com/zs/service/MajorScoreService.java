package com.zs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.entity.MajorScore;
import java.util.List;

/**
 * 专业分数线服务接口
 */
public interface MajorScoreService extends IService<MajorScore> {
    /**
     * 根据学校ID查询专业分数线
     */
    List<MajorScore> getBySchoolId(Long schoolId);

    /**
     * 根据省份查询专业分数线
     */
    List<MajorScore> getByProvince(String province);
    
    /**
     * 根据专业名称查询专业分数线
     */
    List<MajorScore> getByMajorName(String majorName);
    
    /**
     * 根据年份、学校ID和专业名称查询专业分数线
     */
    List<MajorScore> getByYearAndSchoolIdAndMajorName(Integer year, Long schoolId, String majorName);
    
    /**
     * 填充历史分数数据（迁移AdmissionPolicy数据到MajorScore表）
     */
    boolean fillHistoricalScoreData();
} 