package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    /**
     * 查询今日运营数据
     * @param beginTime
     * @param endTime
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 查询套餐总览
     * @return
     */
    SetmealOverViewVO getOverviewSetmeals();

    /**
     * 菜品总览
     * @return
     */
    DishOverViewVO getOverviewDishes();

    /**
     * 订单管理数据
     * @return
     */
    OrderOverViewVO getOverviewOrders();

}
