package com.wdm.service;

import com.wdm.model.User;

/*
 * @author wdmyong
 * 20170415
 */
public interface UserService {

    User getById(long id);

    User getByAccountPassword(String account, String password);

    void insert(User user);

    void update(User user);
}
