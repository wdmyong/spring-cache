package com.wdm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wdm.dao.UserDao;
import com.wdm.model.User;
import com.wdm.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

    @Override
    public User getById(Integer id) {
        return userDao.getById(id);
    }

}
