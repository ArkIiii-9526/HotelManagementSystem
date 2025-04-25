package com.system.mapper;

import com.system.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectUserByEmail(String email);

    int insertUser(User user);
}
