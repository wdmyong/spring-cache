package com.wdm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.wdm.controller.vo.Response;
import com.wdm.enums.Code;
import com.wdm.service.UserService;
import com.wdm.service.RedisService;

/*
 * @author wdmyong
 * 20170415
 */
public class AbstractController {

    @Resource
    protected UserService userService;

    @Resource
    protected RedisService redisService;

    protected Response success() {
        Response response = new Response();
        response.setCode(Code.SUCCESS);
        return response;
    }

    protected Response errorResponse(Code code) {
        Response response = new Response();
        response.setCode(code);
        return response;
    }

    protected Map<String, Object> geneData(String key, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, object);
        return map;
    }
}
