package com.zs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.entity.College;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CollegeMapper extends BaseMapper<College> {
    // 查询所有院系信息
    List<College> selectAll();
    
    // 根据学校ID查询院系信息
    List<College> selectBySchoolId(Long schoolId);
    
    // 根据名称模糊查询
    List<College> selectByName(String name);
} 