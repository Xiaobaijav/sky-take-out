package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;

@Mapper
public interface UserMapper {

    @Select(value = "select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    @Select(value = "SELECT * FROM user WHERE id = #{userId}")
    User getById(Long userId);

    /**
     * 统计用户数量
     * @param map
     * @return
     */
    Integer countMap(HashMap<Object, Object> map);
}
