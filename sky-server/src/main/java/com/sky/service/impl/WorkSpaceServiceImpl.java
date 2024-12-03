package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 查询今日运营数据
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime beginTime, LocalDateTime endTime) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        // 新增用户数
        Integer newUsers = userMapper.countMap(map);
        // 订单完成率
             // 总订单
        Integer totalOrders = orderMapper.countMap(map);
            // 有效订单
        map.put("status", Orders.COMPLETED);

        // 营业额
        Double turnover  = orderMapper.sumByMap(map);
        turnover = turnover == null ? 0.0 : turnover;
        // 有效订单数
        Integer validOrdersCount = orderMapper.countMap(map);

        Double orderCompleteRate = 0.0;
        Double unitPrice = 0.0;
        if (totalOrders != 0 && validOrdersCount != 0){
            //订单完成率
            orderCompleteRate = (double) (validOrdersCount / totalOrders);
            // 平均客单价
            unitPrice = turnover / validOrdersCount;
        }

        return BusinessDataVO.builder()
                .newUsers(newUsers)
                .validOrderCount(validOrdersCount)
                .orderCompletionRate(orderCompleteRate)
                .unitPrice(unitPrice)
                .turnover(turnover)
                .build();
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    @Override
    public SetmealOverViewVO getOverviewSetmeals() {
        HashMap<String, Integer> map = new HashMap<>();
        // 起售的套餐
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setMealMapper.countByMap(map);

        // 停售的套餐
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setMealMapper.countByMap(map);
        return SetmealOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }

    /**
     * 菜品总览
     *
     * @return
     */
    @Override
    public DishOverViewVO getOverviewDishes() {
        HashMap<String, Integer> map = new HashMap<>();
        // 起售的菜品
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        // 停售的菜品
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);
        return DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }

    /**
     * 订单管理数据
     * @return
     */
    @Override
    public OrderOverViewVO getOverviewOrders() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("beginTime", LocalDateTime.now().with(LocalTime.MIN));
        // 全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);
        // 已取消数量
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);
        // 已完成数量
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);
        // 代派送数量
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);
        // 待接单数量
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }
}
