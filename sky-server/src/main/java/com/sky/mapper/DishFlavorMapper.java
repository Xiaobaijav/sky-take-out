package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入口味数据
     * @param dishFlavorList
     */
    void insertBath(List<DishFlavor> dishFlavorList);

    /**
     * 根据菜品id批量删除口味
     * @param dishIds
     */
    void deleteByDishByIds(List<Long> dishIds);

    /**
     * 根据菜品id查询口味数据
     * @param dishId
     */
    @Select(value = "SELECT * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> selectByDishId(Long dishId);

    /**
     * 根据菜品id删除口味
     * @param dishId
     */
    @Delete(value = "DELETE FROM dish_flavor WHERE dish_id = #{dishId}")
    void deleteByDishById(Long dishId);
}
