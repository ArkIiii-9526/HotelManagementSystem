package com.system.service.Impl;

import com.system.mapper.CustomerMapper;
import com.system.pojo.Customer;
import com.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class customerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> selectPageCustomer(int start,int end) {
        return customerMapper.selectPageCustomer(start,end);
    }

    @Override
    public List<Customer> selectCustomerByName(String name) {
        return customerMapper.selectCustomerByName(name);
    }

    @Override
    public List<Customer> selectCustomerByIdCard(String IdCard) {
        return customerMapper.selectCustomerByIdCard(IdCard);
    }

    @Override
    public int insertCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }

    @Override
    public int updateCustomer(Customer customer) {
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public int deleteCustomer(String idCard) {
        return customerMapper.deleteCustomer(idCard);
    }

    @Override
    public long CountCustomer() {
        return customerMapper.CountCustomer();
    }

    @Override
    public Boolean isCustomerExist(String idCard) {
        return customerMapper.isCustomerExist(idCard) > 0;
    }
}
