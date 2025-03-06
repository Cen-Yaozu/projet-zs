package com.zs.mapper;

import com.zs.entity.MajorInfo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MajorInfoMapper {
    // 插入专业信息
    int insert(MajorInfo majorInfo);

    // 根据ID删除专业信息
    int deleteById(Long id);

    // 更新专业信息
    int update(MajorInfo majorInfo);

    // 根据ID查询专业信息
    MajorInfo selectById(Long id);

    // 查询所有专业信息
    List<MajorInfo> selectAll();

    // 根据学校ID查询专业信息
    List<MajorInfo> selectBySchoolId(Long schoolId);

    // 根据专业名称模糊查询
    List<MajorInfo> selectByName(String name);

    // 根据专业类别查询
    List<MajorInfo> selectByCategory(String category);

    // 根据专业类型查询
    List<MajorInfo> selectByType(String type);
}