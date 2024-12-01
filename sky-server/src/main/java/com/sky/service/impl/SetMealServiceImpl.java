package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVO = setMealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(setmealVO.getTotal(), setmealVO.getResult());
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void setWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 向套餐表中插入数据
        setMealMapper.insert(setmeal);

        //获取生成的套餐id
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        setmealDishList.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        // 保存套餐关联的菜品
        setMealDishMapper.insertBatch(setmealDishList);

    }

    /**
     * 停售或起售套餐
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //有停售菜品提示“套餐内包含未起售菜品，无法起售”
        if (status == StatusConstant.ENABLE){
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (!dishList.isEmpty()){
                dishList.forEach(dish -> {
                    if (dish.getStatus() == StatusConstant.DISABLE){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setMealMapper.updateById(setmeal);
    }

    /**
     * 根据ID查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO selectById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        //查询套餐
        Setmeal setmeal = setMealMapper.getById(id);
        // 套餐菜品查询
        List<SetmealDish> setmealDishList = setMealDishMapper.getSetmealDishBySetmealId(id);
        // 属性赋值
        BeanUtils.copyProperties(setmeal, setmealVO);
       setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        //修改套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.updateById(setmeal);

        // 先删除原来的菜品
        Long setmealId = setmeal.getId();
        setMealDishMapper.deleteBatchBySetmealID(setmealId);

        // 味菜品设置套餐id
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        setmealDishList.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        // 添加新的菜品
        setMealDishMapper.insertBatch(setmealDishList);
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void delBatch(List<Long> ids) {
        // 判断当前套餐是否在售
        ids.forEach(id -> {
            Setmeal setmeal = setMealMapper.getById(id);
            if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)){
                throw new SetmealEnableFailedException(MessageConstant.DISH_ON_SALE);
            }
        });

        //删除该套餐的菜品
        setMealMapper.delBatch(ids);
        // 删除该套餐
        for (Long id: ids){
            setMealDishMapper.deleteBatchBySetmealID(id);
        }
    }

    /**
     * 根据套餐id查询包含的菜品
     *
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemById(id);
    }

    /**
     * 根据分类ID查询套餐
     *
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        return setMealMapper.getCatecoryById(setmeal);
    }
}
