package com.wdm.controller;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;
import com.wdm.controller.vo.Response;
import com.wdm.model.User;

/*
 * @author wdmyong
 * 20170415
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {

    @Resource
    DruidDataSource myDataSource;

    @RequestMapping("/json")
    public Response json() throws SQLException {
        Response response = success();
        response.setData(geneData("v", myDataSource.getConnection()));
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response get(@PathVariable("id") Integer id) {
        Response response = success();
        response.setData(geneData("user", userService.getById(id)));
        return response;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Response insert(User user) {
        Response response = success();
        userService.insert(user);
        response.setData(geneData("user", user));
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Response update(@PathVariable("id") Integer id, User user) {
        Response response = success();
        user.setId(id);
        userService.update(user);
        response.setData(geneData("user", user));
        return response;
    }
}
