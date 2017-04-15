package com.wdm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
