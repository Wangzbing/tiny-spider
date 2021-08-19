package com.tiny.cloud.spider.common.message;


import cn.hutool.core.util.StrUtil;

/**
 * @author Amin
 */
public class InterfaceMessage extends ApplicationMessage {


    public static final int ILLEGAL_SIGNATURE_CODE = 500;
    public static final String ILLEGAL_SIGNATURE_10101_MESSAGE = " Illegal signature ";

    public static InterfaceMessage illegalSignature(String message) {
        return new InterfaceMessage(ILLEGAL_SIGNATURE_CODE,
                StrUtil.isEmpty(message) ? ILLEGAL_SIGNATURE_10101_MESSAGE : message);
    }

    private InterfaceMessage(int code, String message) {
        super(code, message);
    }
}
