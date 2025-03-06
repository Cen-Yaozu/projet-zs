package com.zs.service.impl;

import com.zs.entity.MajorInfo;
import com.zs.mapper.MajorInfoMapper;
import com.zs.service.MajorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MajorInfoServiceImpl implements MajorInfoService {

    @Autowired
    private MajorInfoMapper majorInfoMapper;

    @Override
    public boolean addMajorInfo(MajorInfo majorInfo) {
        return majorInfoMapper.insert(majorInfo) > 0;
    }

    @Override
    public boolean deleteMajorInfo(Long id) {
        return majorInfoMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateMajorInfo(MajorInfo majorInfo) {
        return majorInfoMapper.update(majorInfo) > 0;
    }

    @Override
    public MajorInfo getMajorInfoById(Long id) {
        return majorInfoMapper.selectById(id);
    }

    @Override
    public List<MajorInfo> getAllMajorInfos() {
        return majorInfoMapper.selectAll();
    }

    @Override
    public List<MajorInfo> getMajorInfosBySchoolId(Long schoolId) {
        return majorInfoMapper.selectBySchoolId(schoolId);
    }

    @Override
    public List<MajorInfo> getMajorInfosByName(String name) {
        return majorInfoMapper.selectByName(name);
    }

    @Override
    public List<MajorInfo> getMajorInfosByCategory(String category) {
        return majorInfoMapper.selectByCategory(category);
    }

    @Override
    public List<MajorInfo> getMajorInfosByType(String type) {
        return majorInfoMapper.selectByType(type);
    }
}