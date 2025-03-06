package com.zs.service.impl;

import com.zs.entity.SchoolInfo;
import com.zs.mapper.SchoolInfoMapper;
import com.zs.service.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SchoolInfoServiceImpl implements SchoolInfoService {

    @Autowired
    private SchoolInfoMapper schoolInfoMapper;

    @Override
    public boolean addSchoolInfo(SchoolInfo schoolInfo) {
        return schoolInfoMapper.insert(schoolInfo) > 0;
    }

    @Override
    public boolean deleteSchoolInfo(Long id) {
        return schoolInfoMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateSchoolInfo(SchoolInfo schoolInfo) {
        return schoolInfoMapper.update(schoolInfo) > 0;
    }

    @Override
    public SchoolInfo getSchoolInfoById(Long id) {
        return schoolInfoMapper.selectById(id);
    }

    @Override
    public List<SchoolInfo> getAllSchoolInfos() {
        return schoolInfoMapper.selectAll();
    }

    @Override
    public List<SchoolInfo> getSchoolInfosByName(String name) {
        return schoolInfoMapper.selectByName(name);
    }

    @Override
    public List<SchoolInfo> getSchoolInfosByProvince(String province) {
        return schoolInfoMapper.selectByProvince(province);
    }

    @Override
    public List<SchoolInfo> getSchoolInfosByType(String type) {
        return schoolInfoMapper.selectByType(type);
    }
}