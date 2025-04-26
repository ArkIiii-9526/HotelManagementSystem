package com.system.service;

import com.system.config.SpringConfig;
import com.system.pojo.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;


@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfig.class)
public class CustomerServiceTest {
    //TODO: Test cases for customerService

    @Autowired
    CustomerService customerService;

    @Test
    public void selectPageCustomerTest() {
        int start = 0;
        int end = 10;
        List<Customer> testCustomer = customerService.selectPageCustomer(start, end);
        for (Customer customer : testCustomer) {
            System.out.println(customer.getName());
            System.out.println(customer.getSex());
            System.out.println(customer.getAge());
            System.out.println(customer.getPhone());
            System.out.println(customer.getIdCard());
        }
    }
}
