package com.system.service;

import com.system.pojo.User;
public interface UserService {

    User selectUserByEmail(String email);

    int insertUser(User user);

    boolean isUserExist(String email);


}
