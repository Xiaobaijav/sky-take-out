package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐分类接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetMealService setMealService;


    /**
     * 根据分类ID查询套餐
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类ID查询套餐")
    public Result<List<Setmeal>> list(Long categoryId){
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);
        return Result.success(setMealService.list(setmeal));
    }



    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation(value = "根据套餐id查询包含的菜品")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id){
        List<DishItemVO> dishItemVOS = setMealService.getDishItemById(id);
        return Result.success(dishItemVOS);
    }


}
