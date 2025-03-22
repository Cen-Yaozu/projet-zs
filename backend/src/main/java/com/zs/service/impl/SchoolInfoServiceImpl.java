package com.zs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.SchoolInfo;
import com.zs.mapper.SchoolInfoMapper;
import com.zs.service.SchoolInfoService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SchoolInfoServiceImpl extends ServiceImpl<SchoolInfoMapper, SchoolInfo> implements SchoolInfoService {

    @Override
    public List<SchoolInfo> searchByName(String name) {
        return list(new LambdaQueryWrapper<SchoolInfo>()
                .like(SchoolInfo::getName, name));
    }

    @Override
    public List<SchoolInfo> getByProvince(String province) {
        return list(new LambdaQueryWrapper<SchoolInfo>()
                .eq(SchoolInfo::getProvince, province));
    }

    @Override
    public List<SchoolInfo> getByType(String type) {
        return list(new LambdaQueryWrapper<SchoolInfo>()
                .eq(SchoolInfo::getType, type));
    }
}