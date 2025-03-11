package com.zs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.entity.MajorInfo;
import java.util.List;

public interface MajorInfoService extends IService<MajorInfo> {
    // 根据学校ID查询专业信息
    List<MajorInfo> getBySchoolId(Long schoolId);

    // 根据专业名称模糊查询
    List<MajorInfo> searchByName(String name);

    // 根据学位类型查询
    List<MajorInfo> getByDegree(String degree);
}