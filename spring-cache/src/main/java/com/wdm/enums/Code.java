package com.wdm.enums;

/*
 * @author wdmyong
 * 20170415
 */
public enum Code {

    SUCCESS(200, "成功"),
    PARAM_INVALID(601, "参数非法"),
    ACCOUNT_INVALID(602, "账号无效"),
    PASSWORD_INVALID(603, "密码不正确"),
    ACCOUNT_OR_PASSWORD_INVALID(604, "账号或密码不正确"),
    ;

    private int code;
    private String msg;

    Code(int code, String msg) {
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
