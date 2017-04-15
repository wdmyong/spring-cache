package com.wdm.service;

import com.wdm.model.User;

/*
 * @author wdmyong
 * 20170415
 */
public interface UserService {

    public User getById(Integer id);

    public void insert(User user);

    public void update(User user);
}
