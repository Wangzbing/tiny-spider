package com.tiny.spider.common.web;


import java.util.Arrays;

/**
 * @author Amin
 */
public class ResponseErrors {

    private static final int ERROR_SIZE = 0xFF;
    private final ErrorNode[] errors = new ErrorNode[ERROR_SIZE];
    private int pointer = 0;

    public final void addError(int code, String message) {
        if (pointer < ERROR_SIZE) {
            errors[pointer++] = new ErrorNode(code, message);
        } else {
            throw new ArrayStoreException(" error size  over length, max length is :" + ERROR_SIZE);
        }
    }

    public final void addError(String message) {
        if (pointer < ERROR_SIZE) {
            errors[pointer++] = new ErrorNode(message);
        } else {
            throw new ArrayStoreException(" error size  over length, max length is :" + ERROR_SIZE);
        }
    }

    public ErrorNode[] getErrors() {
        return Arrays.copyOf(errors,pointer);
    }

    public static class ErrorNode {
        private int code;
        private final String details;

        public ErrorNode(String details){
            this.details = details;
        }

        public ErrorNode(int code, String details) {
            this.code = code;
            this.details = details;
        }

        public int getCode() {
            return code;
        }

        public String getDetails() {
            return details;
        }
    }

}
