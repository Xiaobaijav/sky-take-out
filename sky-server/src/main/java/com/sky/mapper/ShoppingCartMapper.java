package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 添加购物车
     * @param shoppingCart
     */
    @Insert(value = "INSERT INTO shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUES " +
            "(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户ID动态查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> selectByUserIdAndSetmealIdOrDishId(ShoppingCart shoppingCart);

    /**
     * 根据ID修改商品数量
     * @param shoppingCart
     */
    @Insert(value = "UPDATE shopping_cart set number = #{number} where id = #{id}")
    void updateById(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete(value = "DELETE FROM shopping_cart where user_id = #{userId}")
    void delByUserId(Long userId);

    /**
     * 批量插入购物车
     * @param shoppingCartList
     */
    void inserBatch(List<ShoppingCart> shoppingCartList);

}
