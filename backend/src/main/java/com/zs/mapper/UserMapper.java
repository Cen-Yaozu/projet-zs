package com.zs.mapper;

import com.zs.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
    // 插入用户
    int insert(User user);

    // 根据ID删除用户
    int deleteById(Long id);

    // 更新用户信息
    int update(User user);

    // 根据ID查询用户
    User selectById(Long id);

    // 查询所有用户
    List<User> selectAll();

    // 根据用户名查询用户
    User selectByUsername(String username);

    // 根据用户名和密码查询用户
    User selectByUsernameAndPassword(String username, String password);
}