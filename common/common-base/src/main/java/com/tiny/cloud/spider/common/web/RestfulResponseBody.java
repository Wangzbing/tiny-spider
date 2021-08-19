package com.tiny.cloud.spider.common.web;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @description:
 * @author: zlf
 * @Date: 2020-07-10
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestfulResponseBody<T> implements Serializable {

    private static final long serialVersionUID = 2097609128603129250L;

    /**
     * code
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 错误
     */
    private ResponseErrors.ErrorNode[] errors;

    /**
     * 数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseErrors.ErrorNode[] getErrors() {
        return errors;
    }

    public void setErrors(ResponseErrors.ErrorNode[] errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        RestfulResponseBody<?> that = (RestfulResponseBody<?>) o;
        return code == that.code && Objects.equals(message, that.message) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, data);
    }

    @Override
    public String toString() {
        return "RestfulResponseBody{" + "code=" + code + ", message='" + message + '\'' + ", errors=" + Arrays.toString(errors) + ", data=" + data + '}';
    }
}
