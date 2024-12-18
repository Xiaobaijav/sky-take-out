package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理相关接口")
public class DishController {


    @Autowired
    private DishService dishService;
    
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result<DishDTO> save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        
        // 清理缓存数据
        String key = "dish_" + dishDTO.getId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 菜品分类分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "菜品分类分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用或停售菜品
     * @param status 状态
     * @param id 当前菜品id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用或停售菜品")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        dishService.startOrStop(status, id);

        // 清理缓存数据
        cleanCache("dish*");
        return Result.success();
    }


    /**
     * 根据id修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "根据id修改菜品")
    public Result<String> updateWithFlavorById(@RequestBody DishDTO dishDTO){
        dishService.updateWithFlavorById(dishDTO);
        // 清理缓存数据
        cleanCache("dish*");
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品")
    public Result<DishVO> selectById(@PathVariable("id") Long id){
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类ID查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除菜品")
    public Result delBatch(@RequestParam List<Long> ids){
        dishService.delBatch(ids);

        //删除所有以dish_开头的key
        cleanCache("dish*");
        return Result.success();
    }

    /**
     * 删除缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
