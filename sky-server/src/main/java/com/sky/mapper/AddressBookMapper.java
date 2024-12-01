package com.sky.mapper;

import com.sky.entity.AddressBook;
import javafx.scene.chart.ValueAxis;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 根据ID查询用户地址
     * @param addressBook
     * @return
     */
    List<AddressBook> selectByUserId(AddressBook addressBook);

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert(value = "INSERT INTO address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values " +
            "(#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void add(AddressBook addressBook);

    /**
     * 删除地址
     * @param id
     * @param userId
     */
    @Delete(value = "DELETE FROM address_book where id = #{id} and user_id = #{userId}")
    void delById(Long id, Long userId);

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
    @Select(value = "select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 设置默认地址为非默认地址
     * @param addressBook
     */
    @Update(value = "update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateDefaultByUserId(AddressBook addressBook);
}
