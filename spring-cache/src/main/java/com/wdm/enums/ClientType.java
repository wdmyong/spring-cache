package com.wdm.enums;

import java.util.Arrays;

/**
 * @author wdmyong
 * 2020-07-12
 */
public enum ClientType {

    UNKNOWN(0, "未知类型"),
    APP(1, "app"),
    WEB(2, "web"),
    ;

    private int type;
    private String desc;

    ClientType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ClientType fromValue(int type) {
        return Arrays.stream(values()).filter(t -> t.type == type).findFirst().orElse(UNKNOWN);
    }
}
