package com.sky.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 存放指定范围内的日期
        List<LocalDate> dataList = new ArrayList<>();

        dataList.add(begin);
        while (!begin.equals(end)){
            // 计算指定日期的后一天
            begin = begin.plusDays(1);
            dataList.add(begin);
        }
        // 计算dataList 查询每一天的营业额
        ArrayList<Double> turnoverList = new ArrayList<>(); // 存放每天的营业额
        for (LocalDate date : dataList) {
            // 查询date日期对应的营业额
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // select sum(ammount) from orders where order_time > beginTime and order_time endTime and status = 5
            HashMap<Object, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);
            Double amount = orderMapper.sumByMap(map);
            amount = amount == null ? 0.0 : amount;
            turnoverList.add(amount);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dataList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 存放指定范围内的日期
        List<LocalDate> dataList = new ArrayList<>();

        dataList.add(begin);
        while (!begin.equals(end)){
            // 计算指定日期的后一天
            begin = begin.plusDays(1);
            dataList.add(begin);
        }
        ArrayList<Integer> allUserList = new ArrayList<>();
        ArrayList<Integer> newUserList = new ArrayList<>();
        for (LocalDate date : dataList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 0:0
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 23:59

            Integer allUser = getUserCount(null, endTime);
            Integer newUser = getUserCount(beginTime, endTime);
            allUserList.add(allUser);
            newUserList.add(newUser);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dataList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(allUserList, ",")
                ).build();
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 存放指定范围内的日期
        List<LocalDate> dataList = new ArrayList<>();

        dataList.add(begin);
        while (!begin.equals(end)){
            // 计算指定日期的后一天
            begin = begin.plusDays(1);
            dataList.add(begin);
        }
        // 每天的订单量
        ArrayList<Integer> orderCountList = new ArrayList<>();
        // 每天的有效订单量
        ArrayList<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dataList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 所有订单
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            // 有效订单
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        // 时间内订单总数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        // 时间内有效订单数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        // 订单完成率
        Double orderCompletionRate = 0.0;
        if (validOrderCount != 0){
            orderCompletionRate = (double) (validOrderCount / totalOrderCount);
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dataList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .build();
    }

    /**
     * 销量排名前十
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime);

        String nameList = StringUtils
                .join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()), ",");
        String numberList = StringUtils
                .join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()), ",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("status", status);
        return orderMapper.countMap(map);
    }

    private Integer getUserCount(LocalDateTime beginTime, LocalDateTime endTime) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return userMapper.countMap(map);
    }


}
