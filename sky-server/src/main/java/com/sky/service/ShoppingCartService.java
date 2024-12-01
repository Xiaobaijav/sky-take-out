package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void insert(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @param currentId
     * @return
     */
    List<ShoppingCart> selectByUserId();

    /**
     * 清空购物车
     */
    void del();
}
