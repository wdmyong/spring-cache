package com.wdm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdm.controller.vo.Response;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response get(@PathVariable("id") Integer id) {
        Response response = success();
        response.setData(geneData("user", userService.getById(id)));
        return response;
    }

    @ResponseBody
    @RequestMapping("/index")
    public String index() {
        return "user hello world";
    }
}
