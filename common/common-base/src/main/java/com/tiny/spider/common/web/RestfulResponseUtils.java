package com.tiny.spider.common.web;


import cn.hutool.core.text.CharSequenceUtil;
import com.tiny.spider.common.message.ApplicationMessage;
import com.tiny.spider.common.message.SuccessMessage;

/**
 * @author zhenglinfeng
 */
public final class RestfulResponseUtils {

    public static <T> RestfulResponseBody<T> successResponse() {
        RestfulResponseBody<T> response = new RestfulResponseBody<>();
        response.setMessage(SuccessMessage.successful().getMessage());
        response.setCode(SuccessMessage.successful().getCode());
        return response;
    }


    public static <T> RestfulResponseBody<T> successResponse(T data, String message) {
        RestfulResponseBody<T> response = new RestfulResponseBody<>();
        if (CharSequenceUtil.isNotEmpty(message)) {
            response.setMessage(message);
        } else {
            response.setMessage(SuccessMessage.successful().getMessage());
        }
        response.setCode(SuccessMessage.successful().getCode());
        response.setData(data);

        return response;
    }

    public static <T> RestfulResponseBody<T> errorResponse(ApplicationMessage applicationMessage) {
        RestfulResponseBody<T> response = new RestfulResponseBody<>();
        response.setCode(applicationMessage.getCode());
        response.setMessage(applicationMessage.getMessage());
        return response;
    }

    public static <T> RestfulResponseBody<T> errorResponse(ApplicationMessage applicationMessage, String... errorDetails) {
        RestfulResponseBody<T> response = new RestfulResponseBody<>();
        ResponseErrors errors = new ResponseErrors();
        if (null != errorDetails) {
            for (String error : errorDetails) {
                errors.addError(500,error);
                response.setErrors(errors.getErrors());
            }
        }
        response.setCode(applicationMessage.getCode());
        response.setMessage(applicationMessage.getMessage());
        return response;
    }

    private RestfulResponseUtils() {
    }
}
