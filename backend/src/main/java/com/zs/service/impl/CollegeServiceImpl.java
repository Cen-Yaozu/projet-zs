package com.zs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.College;
import com.zs.mapper.CollegeMapper;
import com.zs.service.CollegeService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {

    @Override
    public List<College> getBySchoolId(Long schoolId) {
        return list(new LambdaQueryWrapper<College>()
                .eq(College::getSchoolId, schoolId));
    }

    @Override
    public List<College> searchByName(String name) {
        return list(new LambdaQueryWrapper<College>()
                .like(College::getName, name));
    }
} 