package com.wdm.utils;

/**
 * @author wdmyong
 */
public final class LoginUserIdContextHolder {

    private LoginUserIdContextHolder() {
    }

    private static final ThreadLocal<Long> contextHolder = new ThreadLocal<>();

    public static void setUserId(long userId) {
        contextHolder.set(userId);
    }

    public static long getUserId() {
        return contextHolder.get();
    }

    public static void clearUserId() {
        contextHolder.remove();
    }
}
