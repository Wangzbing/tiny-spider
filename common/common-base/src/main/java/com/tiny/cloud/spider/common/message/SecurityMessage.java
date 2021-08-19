package com.tiny.cloud.spider.common.message;


import cn.hutool.core.util.StrUtil;

/**
 * @author zhenglinfeng
 */
public class SecurityMessage extends ApplicationMessage {
    public static final int PASSWORD_ERROR_CODE = 410;
    public static final String PASSWORD_ERROR_10301_MESSAGE = "Auth Fail ";

    public static final int ORIGINAL_PASSWORD_ERROR_CODE = 430;
    public static final String ORIGINAL_PASSWORD_ERROR_10302_MESSAGE = "Token expire or not exist";


    public static SecurityMessage AuthFail(String message) {
        return new SecurityMessage(PASSWORD_ERROR_CODE,
                StrUtil.isEmpty(message) ? PASSWORD_ERROR_10301_MESSAGE : message );
    }

    public static SecurityMessage TokenError(String message) {
        return new SecurityMessage(ORIGINAL_PASSWORD_ERROR_CODE,
                StrUtil.isEmpty(message) ? ORIGINAL_PASSWORD_ERROR_10302_MESSAGE : message );
    }
    private SecurityMessage(int code, String message) {
        super(code, message);
    }
}
