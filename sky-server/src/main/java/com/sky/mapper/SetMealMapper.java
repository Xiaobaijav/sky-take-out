package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface SetMealMapper {


    /**
     * 查询当前套餐下是否有菜品
     * @param categoryId
     * @return
     */
    @Select(value = "select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 修改套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void updateById(Setmeal setmeal);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Select(value = "select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除套餐
     * @param ids
     */
    void delBatch(List<Long> ids);

    /**
     * 根据套餐id查询对应的菜品
     * @param setmealId
     * @return
     */
    @Select(value = "SELECT\n" +
            "  sd.copies,\n" +
            "  d.`name`,\n" +
            "  d.description,\n" +
            "  d.image \n" +
            "FROM\n" +
            "  setmeal_dish sd\n" +
            "  LEFT JOIN dish d ON sd.dish_id = d.id \n" +
            "WHERE\n" +
            "  sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemById(Long setmealId);

    /**
     * 根据分类ID查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> getCatecoryById(Setmeal setmeal);

    /**
     * 查询套餐
     * @param map
     * @return
     */
    Integer countByMap(HashMap<String, Integer> map);

}
