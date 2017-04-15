package com.wdm.enums;

/*
 * @author wdmyong
 * 20170415
 */
public enum Code {

    SUCCESS(200, "成功");

    private int code;
    private String msg;

    private Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
