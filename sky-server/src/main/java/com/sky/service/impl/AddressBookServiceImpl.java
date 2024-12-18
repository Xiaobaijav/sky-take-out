package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    @Override
    public List<AddressBook> select(AddressBook addressBook) {
        return addressBookMapper.selectByUserId(addressBook);
    }

    /**
     * 新增用户地址
     *
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.add(addressBook);
    }

    /**
     * 删除地址
     *
     * @param id
     */
    @Override
    public void del(Long id) {
        addressBookMapper.delById(id, BaseContext.getCurrentId());
    }

    /**
     * 修改地址
     *
     * @param addressBook
     */
    @Override
    @Transactional
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        // 先将当前用户的默认地址设置为非默认地址
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateDefaultByUserId(addressBook);
        //设置当前地址为默认地址
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }
}
