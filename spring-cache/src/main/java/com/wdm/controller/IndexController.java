package com.wdm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdm.controller.vo.Response;

/*
 * @author wdmyong
 * 20170415
 */
@Controller
@RequestMapping("")
public class IndexController extends AbstractController {

    @ResponseBody
    @RequestMapping("/")
    public String home() {
        return "welcome to my web";
    }

    @ResponseBody
    @RequestMapping("/index")
    public String index() {
        return "hello world";
    }

    @ResponseBody
    @RequestMapping("json")
    public Response json() {
        return success();
    }
}
