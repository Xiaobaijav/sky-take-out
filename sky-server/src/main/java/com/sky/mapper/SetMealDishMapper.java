package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 套餐分类
 */
@Mapper
public interface SetMealDishMapper {

    /**
     * 查询菜品是否在某个套餐中
     * @param dishIds
     * @return
     */
    List<Long> getSetMealDishIds(List<Long> dishIds);


    /**
     * 保存套餐关联的菜品
     * @param setmealDishList
     */
    @AutoFill(OperationType.INSERT)
    void insertBatch(List<SetmealDish> setmealDishList);


    /**
     * 根据套餐id删除
     * @param setmealId
     */
    @Delete(value = "DELETE from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBatchBySetmealID(Long setmealId);


    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select(value = "SELECT * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getSetmealDishBySetmealId(Long setmealId);
}
