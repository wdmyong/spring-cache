package com.wdm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.wdm.controller.vo.Response;
import com.wdm.enums.Code;
import com.wdm.service.UserService;

public class AbstractController {

    @Resource
    protected UserService userService;

    protected Response success() {
        Response response = new Response();
        response.setCode(Code.SUCCESS);
        return response;
    }

    protected Map<String, Object> geneData(String key, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, object);
        return map;
    }
}
