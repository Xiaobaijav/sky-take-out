package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 查询当前用户地址
     * @return
     */
    List<AddressBook> select(AddressBook addressBook);

    /**
     * 新增用户地址
     * @param addressBook
     */
    void add(AddressBook addressBook);

    /**
     * 删除地址
     * @param id
     */
    void del(Long id);

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);
}
