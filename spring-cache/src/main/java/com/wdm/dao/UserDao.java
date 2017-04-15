package com.wdm.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.wdm.mapper.UserMapper;
import com.wdm.model.User;

/*
 * @author wdmyong
 * 20170415
 */
@Repository
public class UserDao {

    @Resource
    UserMapper userMapper;

    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    public void insert(User user) {
        userMapper.insert(user);
    }

    public void update(User user) {
        
        userMapper.update(user);
    }
}
