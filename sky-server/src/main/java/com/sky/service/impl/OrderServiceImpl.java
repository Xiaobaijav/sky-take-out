package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 订单提交
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //1、 处理业务异常
            // a、地址是否为空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
            // b、购物车是否为空
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(currentId)
                .build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectByUserIdAndSetmealIdOrDishId(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //2、 向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now()); // 下单时间
        orders.setPayStatus(Orders.UN_PAID); //支付状态
        orders.setStatus(Orders.PENDING_PAYMENT); // 订单状态
        orders.setNumber(String.valueOf(System.currentTimeMillis())); //订单号
        orders.setPhone(addressBook.getPhone()); //手机号
        orders.setConsignee(addressBook.getConsignee()); //收货人
        orders.setUserId(currentId);; //用户ID
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail()); //地址详情

        orderMapper.insert(orders);
        //3、 向订单明细表插入n条数据
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart: shoppingCartList){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId()); //设置当前订单明细关联的订单ID
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetails);
        //4、 下单成功，清空购物车
        shoppingCartMapper.delByUserId(currentId);

        // 封装返回结果
        return  OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();
    }
}
