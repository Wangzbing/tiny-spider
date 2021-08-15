package com.tiny.spider.common.message;

/**
 * @author Amin
 */
public class SuccessMessage extends ApplicationMessage {

    public static final int SUCCESS_CODE = 200;

    public static SuccessMessage successful(){
        return new SuccessMessage(SUCCESS_CODE, "successful");
    }

    public static SuccessMessage successful(String message){
        return new SuccessMessage(SUCCESS_CODE, message);
    }

    private SuccessMessage(int code, String message) {
        super(code, message);
    }
}
