package com.zs.service;

import com.zs.entity.MajorInfo;
import java.util.List;

public interface MajorInfoService {
    // 添加专业信息
    boolean addMajorInfo(MajorInfo majorInfo);

    // 删除专业信息
    boolean deleteMajorInfo(Long id);

    // 更新专业信息
    boolean updateMajorInfo(MajorInfo majorInfo);

    // 根据ID获取专业信息
    MajorInfo getMajorInfoById(Long id);

    // 获取所有专业信息
    List<MajorInfo> getAllMajorInfos();

    // 根据学校ID获取专业信息
    List<MajorInfo> getMajorInfosBySchoolId(Long schoolId);

    // 根据专业名称模糊查询
    List<MajorInfo> getMajorInfosByName(String name);

    // 根据专业类别查询
    List<MajorInfo> getMajorInfosByCategory(String category);

    // 根据专业类型查询
    List<MajorInfo> getMajorInfosByType(String type);
}