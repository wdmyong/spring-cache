package com.wdm.service.impl;

import java.util.Date;

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
        Date now = new Date();
        user.setCreateTime(now);
        user.setModifyTime(now);
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        user.setModifyTime(new Date());
        userDao.update(user);
    }

}
