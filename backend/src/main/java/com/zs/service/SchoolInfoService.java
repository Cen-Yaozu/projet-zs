package com.zs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.entity.SchoolInfo;
import java.util.List;

public interface SchoolInfoService extends IService<SchoolInfo> {
    // 根据学校名称模糊查询
    List<SchoolInfo> searchByName(String name);

    // 根据省份查询学校
    List<SchoolInfo> getByProvince(String province);

    // 根据学校类型查询
    List<SchoolInfo> getByType(String type);

    // 根据省份和城市查询学校
    List<SchoolInfo> getByProvinceAndCity(String province, String city);

    // 根据学校层次查询（985、211等）
    List<SchoolInfo> getByLevel(String level);
}