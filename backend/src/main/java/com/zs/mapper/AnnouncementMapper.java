package com.zs.mapper;

import com.zs.entity.Announcement;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 通知公告数据访问接口
 */
@Mapper
public interface AnnouncementMapper {
    
    /**
     * 获取所有通知公告
     * @return 通知公告列表
     */
    @Select("SELECT * FROM announcement ORDER BY publish_time DESC")
    List<Announcement> findAll();
    
    /**
     * 根据ID获取通知公告
     * @param id 通知公告ID
     * @return 通知公告对象
     */
    @Select("SELECT * FROM announcement WHERE id = #{id}")
    Announcement findById(Long id);
    
    /**
     * 获取有效的通知公告
     * @return 有效的通知公告列表
     */
    @Select("SELECT * FROM announcement WHERE status = 1 AND (end_time IS NULL OR end_time >= NOW()) ORDER BY type = 'IMPORTANT' DESC, publish_time DESC")
    List<Announcement> findActive();
    
    /**
     * 根据类型获取通知公告
     * @param type 通知公告类型
     * @return 通知公告列表
     */
    @Select("SELECT * FROM announcement WHERE type = #{type} AND status = 1 ORDER BY publish_time DESC")
    List<Announcement> findByType(String type);
    
    /**
     * 添加通知公告
     * @param announcement 通知公告对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO announcement(title, content, image_url, publish_time, end_time, status, type, create_user_id) " +
            "VALUES(#{title}, #{content}, #{imageUrl}, #{publishTime}, #{endTime}, #{status}, #{type}, #{createUserId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Announcement announcement);
    
    /**
     * 更新通知公告
     * @param announcement 通知公告对象
     * @return 影响的行数
     */
    @Update("UPDATE announcement SET title = #{title}, content = #{content}, image_url = #{imageUrl}, " +
            "publish_time = #{publishTime}, end_time = #{endTime}, status = #{status}, type = #{type} WHERE id = #{id}")
    int update(Announcement announcement);
    
    /**
     * 删除通知公告
     * @param id 通知公告ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM announcement WHERE id = #{id}")
    int delete(Long id);
} 