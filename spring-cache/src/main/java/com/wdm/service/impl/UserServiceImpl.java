package com.wdm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wdm.dao.UserDao;
import com.wdm.model.User;
import com.wdm.service.UserService;

/*
 * @author wdmyong
 * 20170415
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

    @Override
    public User getById(Integer id) {
        return userDao.getById(id);
    }

    @Override
    public void insert(User user) {
        long now = System.currentTimeMillis();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(System.currentTimeMillis());
        userDao.update(user);
    }

}
