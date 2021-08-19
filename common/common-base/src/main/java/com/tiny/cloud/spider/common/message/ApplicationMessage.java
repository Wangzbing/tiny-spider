package com.tiny.cloud.spider.common.message;


/**
 * @description:
 * @author: zlf
 * @Date: 2020-03-15
 */
public abstract class ApplicationMessage {

    /**
     * 消息码
     */
    private final int code;


    /**
     * 消息内容
     */
    private String message;


    protected ApplicationMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
