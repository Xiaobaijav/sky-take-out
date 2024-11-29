package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void setWithDish(SetmealDTO setmealDTO);

    /**
     * 停售或起售套餐
     * @param status
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据ID查询套餐
     * @param id
     * @return
     */
    SetmealVO selectById(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 删除套餐
     * @param ids
     */
    void delBatch(List<Long> ids);
}
