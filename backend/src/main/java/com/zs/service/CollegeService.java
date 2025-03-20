package com.zs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.entity.College;
import java.util.List;

public interface CollegeService extends IService<College> {
    // 根据学校ID查询院系信息
    List<College> getBySchoolId(Long schoolId);
    
    // 根据院系名称模糊查询
    List<College> searchByName(String name);
} 