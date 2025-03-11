package com.zs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.entity.SchoolInfo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SchoolInfoMapper extends BaseMapper<SchoolInfo> {
    // 插入学校信息
    int insert(SchoolInfo schoolInfo);

    // 根据ID删除学校信息
    int deleteById(Long id);

    // 更新学校信息
    int update(SchoolInfo schoolInfo);

    // 根据ID查询学校信息
    SchoolInfo selectById(Long id);

    // 查询所有学校信息
    List<SchoolInfo> selectAll();

    // 根据学校名称模糊查询
    List<SchoolInfo> selectByName(String name);

    // 根据省份查询学校
    List<SchoolInfo> selectByProvince(String province);

    // 根据学校类型查询
    List<SchoolInfo> selectByType(String type);
}