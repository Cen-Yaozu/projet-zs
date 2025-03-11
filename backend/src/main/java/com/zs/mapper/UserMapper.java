package com.zs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}