package com.system.mapper;

import com.system.pojo.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {
    List<Customer> selectCustomerByName(String name);
    List<Customer> selectCustomerByIdCard(String IdCard);

    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomer(String idCard);

    long CountCustomer();

    int isCustomerExist(String idCard);
    List<Customer> selectPageCustomer(@Param("start") int start, @Param("end") int end);
}
