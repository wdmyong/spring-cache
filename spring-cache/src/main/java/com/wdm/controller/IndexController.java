package com.wdm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @ResponseBody
    @RequestMapping(value = "/redis/{key}/{value}", method = RequestMethod.POST)
    public Response setCache(@PathVariable("key") String key,
                             @PathVariable("value") String value) {
        Response response = success();
        redisService.setCache(key, value, 360);
        response.setData(geneData(key, value));
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/redis/{key}", method = RequestMethod.GET)
    public Response getCache(@PathVariable("key") String key) {
        Response response = success();
        response.setData(geneData(key, redisService.getCache(key)));
        return response;
    }
}
