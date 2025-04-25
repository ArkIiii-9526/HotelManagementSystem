package com.system.controller;

import com.system.pojo.Customer;
import com.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/api/customers")
    public Map<String, Object> getCustomers(
            @RequestParam(value = "searchName", defaultValue = "") String searchName,
            @RequestParam(value = "currentPage",defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {

        Map<String, Object> result = new HashMap<>();
        try {
            long totalRecords = customerService.CountCustomer();
            // 计算分页参数
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
            int start = (currentPage - 1) * pageSize + 1;
            int end = currentPage * pageSize;

            // 获取分页数据
            List<Customer> list = searchName.isEmpty() ?
                    customerService.selectPageCustomer(start, end) :
                    customerService.selectCustomerByName(searchName);

            result.put("code", 200);
            result.put("data", Map.of(
                    "list", list,
                    "currentPage", currentPage,
                    "totalPages", totalPages,
                    "totalRecords", totalRecords
            ));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/api/editCustomers/{idCard}")
    public Map<String, Object> updateCustomer(@PathVariable("idCard") String idCard, @RequestBody Customer customer) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (!customerService.isCustomerExist(idCard)) {
                customerService.updateCustomer(customer);
                result.put("code", 200);
                result.put("msg", "客户信息更新成功");
            }else {
                result.put("code", 404);
                result.put("msg", "客户不存在");
            }

        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/api/addCustomers")
    public Map<String, Object> addCustomer(@RequestBody Customer customer) {
        Map<String, Object> result = new HashMap<>();
        try {
            String idCard = customer.getIdCard();
            if (customerService.isCustomerExist(idCard)) {
                result.put("code", 404);
                result.put("msg", "客户已存在");
            }else{
                customerService.insertCustomer(customer);
                result.put("code", 200);
                result.put("msg", "客户添加成功");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/api/deleteCustomers/{idCard}")
    public Map<String, Object> deleteCustomer(@PathVariable("idCard") String idCard) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (customerService.isCustomerExist(idCard)) {
                customerService.deleteCustomer(idCard);
                result.put("code", 200);
                result.put("msg", "客户删除成功");
            }else {
                result.put("code", 404);
                result.put("msg", "客户不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}