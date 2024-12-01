package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址相关接口
 */
@RestController("AddressBookController")
@RequestMapping("/user/addressBook")
@Api(tags = "地址相关接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    /**
     *查询当前用户所有地址
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询当前用户地址")
    public Result<List<AddressBook>> select(){
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> addressBooks = addressBookService.select(addressBook);
        return Result.success(addressBooks);
    }

    /**
     * 新增地址
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增地址")
    public Result add(@RequestBody AddressBook addressBook){
        addressBookService.add(addressBook);
        return Result.success();
    }

    /**
     * 删除地址
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除地址")
    public Result del(Long id){
        addressBookService.del(id);
        return Result.success();
    }


    /**
     * 根据id查询地址
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "修改地址")
    public Result<AddressBook> selectById(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 修改地址
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改地址")
    public Result update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     *查询当前用户默认地址
     * @return
     */
    @GetMapping("/default")
    @ApiOperation(value = "查询当前用户默认地址")
    public Result<List<AddressBook>> selectDefault(){
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        List<AddressBook> addressBooks = addressBookService.select(addressBook);

        if (!addressBooks.isEmpty()){
            return Result.success(addressBooks);
        }

        return Result.error("没有默认地址");
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result<String> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }




}
