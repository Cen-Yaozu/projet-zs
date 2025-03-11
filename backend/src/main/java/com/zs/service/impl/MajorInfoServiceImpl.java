package com.zs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.MajorInfo;
import com.zs.mapper.MajorInfoMapper;
import com.zs.service.MajorInfoService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MajorInfoServiceImpl extends ServiceImpl<MajorInfoMapper, MajorInfo> implements MajorInfoService {

    @Override
    public List<MajorInfo> getBySchoolId(Long schoolId) {
        return list(new LambdaQueryWrapper<MajorInfo>()
                .eq(MajorInfo::getSchoolId, schoolId));
    }

    @Override
    public List<MajorInfo> searchByName(String name) {
        return list(new LambdaQueryWrapper<MajorInfo>()
                .like(MajorInfo::getName, name));
    }

    @Override
    public List<MajorInfo> getByDegree(String degree) {
        return list(new LambdaQueryWrapper<MajorInfo>()
                .eq(MajorInfo::getDegree, degree));
    }
}