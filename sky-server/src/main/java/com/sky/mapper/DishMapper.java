package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 查询当前菜单分类下是否有菜品
     * @param categoryId
     * @return
     */
    @Select(value = "select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}
