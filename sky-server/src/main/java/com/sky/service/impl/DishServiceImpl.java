package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品口味数据
     *
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        // 向菜品表插入1条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品中插入数据
        dishMapper.insert(dish);

        // 获取插入后生成的主键值
        Long dishId = dish.getId();

        // 向口味表插入数据
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if (!dishFlavorList.isEmpty()){
            dishFlavorList.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBath(dishFlavorList);
        }

    }

    /**
     * 菜品分类分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(dishPage.getTotal(), dishPage.getResult());
    }


    /**
     * 启用或停售菜品
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 根据ID修改菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void updateWithFlavorById(DishDTO dishDTO) {
        // 修改dish表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        // 菜品ID
        Long dishId = dishDTO.getId();

        //修改口味表
        // 1: 删除原有口味
        dishFlavorMapper.deleteByDishById(dishId);
        // 2：向口味表插入数据
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if (!dishFlavorList.isEmpty()){
            dishFlavorList.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBath(dishFlavorList);
        }
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public DishVO selectById(Long id) {
        // 根据id查询菜品数据
        Dish dish = dishMapper.selectById(id);
        // 根据菜品id查询口味数据
        List<DishFlavor> dishFlavorList = dishFlavorMapper.selectByDishId(id);
        //封装数据
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavorList);
        return dishVO;
    }

    /**
     * 根据分类ID查询
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void delBatch(List<Long> ids) {
        // 判断当前菜品能否删除
        for (Long id :ids){
            Dish dish = dishMapper.getById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 当前菜品是否被套餐关联
        List<Long> setMealDishIds = setMealDishMapper.getSetMealDishIds(ids);
        if (!setMealDishIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 批量删除菜品
        dishMapper.deleteByIds(ids);
        //批量删除口味
        dishFlavorMapper.deleteByDishByIds(ids);

    }

    /**
     * 根据分类ID查询对应的菜品以及口味
     *
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        // 查询所有菜品
        List<Dish> dishList = dishMapper.list(dish);
        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            // 查询每一个菜品的口味
            List<DishFlavor> dishFlavorList = dishFlavorMapper.selectByDishId(d.getId());
            dishVO.setFlavors(dishFlavorList);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}
