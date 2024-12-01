package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void insert(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // 设置当前用户ID
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 先查询当前商品是否在购物车中
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserIdAndSetmealIdOrDishId(shoppingCart);
        // 存在：修改数量
        if (!shoppingCarts.isEmpty()){
            ShoppingCart cart = shoppingCarts.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateById(cart);
        }else {
            //不存在：插入一条购物车数据

            // 判断当前购物车加入的是套餐还是菜品
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null){ //是菜品
                Dish dish = dishMapper.getById(dishId);
                // 名称
                shoppingCart.setName(dish.getName());
                // 图片
                shoppingCart.setImage(dish.getImage());
                // 价格
                shoppingCart.setAmount(dish.getPrice());
            }else { //是套餐
                Setmeal setmeal = setMealMapper.getById(shoppingCartDTO.getSetmealId());
                // 名称
                shoppingCart.setName(setmeal.getName());
                // 图片
                shoppingCart.setImage(setmeal.getImage());
                // 价格
                shoppingCart.setAmount(setmeal.getPrice());
            }
            // 数量
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> selectByUserId() {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext
                .getCurrentId())
                .build();
        return shoppingCartMapper.selectByUserIdAndSetmealIdOrDishId(shoppingCart);
    }

    /**
     * 清空购物车
     */
    @Override
    public void del() {
        shoppingCartMapper.delByUserId(BaseContext.getCurrentId());
    }
}
