package com.zs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.entity.AdmissionPolicy;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AdmissionPolicyMapper extends BaseMapper<AdmissionPolicy> {
    // 插入招生政策
    int insert(AdmissionPolicy admissionPolicy);

    // 根据ID删除招生政策
    int deleteById(Long id);

    // 更新招生政策
    int update(AdmissionPolicy admissionPolicy);

    // 根据ID查询招生政策
    AdmissionPolicy selectById(Long id);

    // 查询所有招生政策
    List<AdmissionPolicy> selectAll();

    // 根据学校ID查询招生政策
    List<AdmissionPolicy> selectBySchoolId(Long schoolId);

    // 根据年份和学校ID查询招生政策
    List<AdmissionPolicy> selectByYearAndSchoolId(Integer year, Long schoolId);
}