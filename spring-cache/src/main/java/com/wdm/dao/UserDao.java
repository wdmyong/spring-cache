package com.wdm.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.wdm.mapper.UserMapper;
import com.wdm.model.User;

@Repository
public class UserDao {

    //@Resource
    //UserMapper userMapper;

    public User getById(Integer id) {
        //return userMapper.getById(id);
        return new User();
    }
}
