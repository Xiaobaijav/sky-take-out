package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;


public interface DishService {


    /**
     * 新增菜品口味数据
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分类分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 启用或停售菜品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id修改菜品
     * @param dishDTO
     */
    void updateWithFlavorById(DishDTO dishDTO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    DishVO selectById(Long id);

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return
     */
    Dish list(Integer categoryId);

    /**
     * 批量删除菜品
     * @param ids
     */
    void delBatch(List<Long> ids);
}
