package com.wdm.controller.vo;

import com.wdm.enums.Code;

/*
 * @author wdmyong
 * 20170415
 */
public class Response {

    private int code;
    private String msg;
    private Object data;

    public void setCode(Code code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    
}
