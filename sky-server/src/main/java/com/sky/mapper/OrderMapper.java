package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);

    /** 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 条件分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据订单状态进行数量统计
     * @param status
     */
    @Select(value = "SELECT count(id) from orders where status = #{status}")
    Integer contStatus(Integer status);


    @Update(value = "update orders set status = #{orderStatus}, pay_status = #{orderPaiStatus}, checkout_time = #{checkOutTime} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaiStatus, LocalDateTime checkOutTime, Long id);

    /**
     * 根据订单状态和下单时间查询订单
     * @param status
     * @param orderTime
     * @return
     */
    @Select(value = "SELECT * FROM orders WHERE status = #{status} AND order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    /**
     * 查询每天对应的营业额
     * @param map
     * @return
     */
    Double sumByMap(HashMap<Object, Object> map);

    /**
     * 查询订单数
     * @param map
     * @return
     */
    Integer countMap(HashMap<Object, Object> map);

    /**
     * 销量排行前10统计
     * @param beginTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 订单管理数据
     * @param map
     * @return
     */
    Integer countByMap(HashMap<String, Object> map);

}
