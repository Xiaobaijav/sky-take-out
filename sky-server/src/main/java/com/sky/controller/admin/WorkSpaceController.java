package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 工作台相关接口
 */
@RestController("WorkSpaceController")
@Slf4j
@RequestMapping("/admin/workspace")
@Api(tags = "工作台相关接口")
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;


    /**
     * 今日运营数据
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation(value = "")
    public Result<BusinessDataVO> businessData(){
        // 当天的开始时间和结束时间
        LocalDateTime beginTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData(beginTime, endTime);
        return Result.success(businessDataVO);
    }

    /**
     * 套餐总览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation(value = "套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        SetmealOverViewVO setmealOverViewVO = workSpaceService.getOverviewSetmeals();
        return Result.success(setmealOverViewVO);
    }

    /**
     * 菜品总览
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation(value = "菜品总览")
    public Result<DishOverViewVO> overviewDishes(){
        DishOverViewVO dishOverViewVO = workSpaceService.getOverviewDishes();
        return Result.success(dishOverViewVO);
    }

    /**
     * 订单管理数据
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation(value = "订单管理数据")
    public Result<OrderOverViewVO> overviewOrders(){
        OrderOverViewVO orderOverViewVO = workSpaceService.getOverviewOrders();
        return Result.success(orderOverViewVO);
    }


}
