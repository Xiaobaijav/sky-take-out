package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 套餐分类
 */
@Mapper
public interface SetMealDishMapper {

    List<Long> getSetMealDishIds(List<Long> dishIds);
}
