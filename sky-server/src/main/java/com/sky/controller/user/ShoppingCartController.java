package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("ShoppingCartController")
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车相关接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加购物车")
    public Result insert(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.insert(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     */
    @GetMapping("/list")
    @ApiOperation(value = "查看购物车")
    public Result<List<ShoppingCart>> select(){
        List<ShoppingCart> shoppingCartList = shoppingCartService.selectByUserId();
        return Result.success(shoppingCartList);
    }

    /**
     * 删除购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation(value = "删除购物车")
    public Result clean(){
        shoppingCartService.del();
        return Result.success();
    }

}
