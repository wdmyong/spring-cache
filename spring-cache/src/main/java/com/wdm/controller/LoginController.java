package com.wdm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wdm.controller.vo.Response;

/*
 * @author wdmyong
 * 2020-07-09
 */
@RestController
@RequestMapping("")
public class LoginController extends AbstractController {

    // 返回token
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response appLogin(String userName, String password) {
        Response response = success();

        return response;
    }

    // 种cookie
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void webLogin(String userName, String password, HttpServletRequest request, HttpServletResponse response) {
    }
}
