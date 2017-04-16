package com.wdm.dao;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "user_cache_", key = "'id_' + #id")
    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    public void insert(User user) {
        userMapper.insert(user);
    }

    @CacheEvict(value = "user_cache_", key = "'id_' + #user.id")
    public void update(User user) {
        userMapper.update(user);
    }
}
