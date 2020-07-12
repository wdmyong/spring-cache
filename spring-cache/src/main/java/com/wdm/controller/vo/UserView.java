package com.wdm.controller.vo;

import com.wdm.model.User;

/**
 * @author wdmyong
 * 2020-07-12
 */
public class UserView {

    private long id;
    private String name;
    private String account;
    private long time;

    private UserView(long id, String name, String account, long time) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public long getTime() {
        return time;
    }

    public static UserView of(User user) {
        return new UserView(user.getId(), user.getName(), user.getAccount(), user.getUpdateTime());
    }
}
