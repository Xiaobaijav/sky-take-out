package com.sky.controller.admin;
import java.util.Optional;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 营业状态相关接口
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "营业状态相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation(value = "设置营业状态")
    public Result setStatus(@PathVariable Integer status){
        // status==1? 营业中:打烊
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "查询营业状态")
    public Result<Integer> getStatus(){
        Integer shopStatus = (Integer) Optional.ofNullable(redisTemplate.opsForValue().get("SHOP_STATUS")).orElse(0);
        return Result.success(shopStatus);
    }
}
