package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每分钟定时处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOut(){

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        // 查看状态为待付款状态的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);
        // 超过15分钟的取消订单
        if (ordersList != null && !ordersList.isEmpty()){
            for (Orders order : ordersList){
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单已超时");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 每天凌晨1点处理处于配送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?") //每天凌晨一点触发一次
    public void processDeliveryOrder(){
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        // 查看状态为待付款状态的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);
        // 超过15分钟的取消订单
        if (ordersList != null && !ordersList.isEmpty()){
            for (Orders order : ordersList){
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }


    }
}
