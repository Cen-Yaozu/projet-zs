package com.zs.service;

import com.zs.entity.SchoolInfo;
import java.util.List;

public interface SchoolInfoService {
    // 添加学校信息
    boolean addSchoolInfo(SchoolInfo schoolInfo);

    // 删除学校信息
    boolean deleteSchoolInfo(Long id);

    // 更新学校信息
    boolean updateSchoolInfo(SchoolInfo schoolInfo);

    // 根据ID获取学校信息
    SchoolInfo getSchoolInfoById(Long id);

    // 获取所有学校信息
    List<SchoolInfo> getAllSchoolInfos();

    // 根据学校名称模糊查询
    List<SchoolInfo> getSchoolInfosByName(String name);

    // 根据省份查询学校
    List<SchoolInfo> getSchoolInfosByProvince(String province);

    // 根据学校类型查询
    List<SchoolInfo> getSchoolInfosByType(String type);
}