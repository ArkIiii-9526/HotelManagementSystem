package com.system.service.Impl;

import com.system.mapper.UserMapper;
import com.system.pojo.User;
import com.system.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class userServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public boolean isUserExist(String email) {
        if (userMapper.selectUserByEmail(email) != null) {
            return true;
        }
        return false;
    }
}
