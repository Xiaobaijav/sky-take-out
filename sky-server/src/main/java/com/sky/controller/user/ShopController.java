package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 营业状态相关接口
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "C端-营业状态相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "查询营业状态")
    public Result<Integer> setStatus(){
        Integer shopStatus = (Integer) Optional.ofNullable(redisTemplate.opsForValue().get(KEY)).orElse(0);
        return Result.success(shopStatus);
    }
}
