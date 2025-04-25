package com.system.service;

import com.system.pojo.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> selectPageCustomer(int start,int end);
    List<Customer> selectCustomerByName(String name);
    List<Customer> selectCustomerByIdCard(String IdCard);

    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomer(String idCard);

    long CountCustomer();

    Boolean isCustomerExist(String idCard);
}
