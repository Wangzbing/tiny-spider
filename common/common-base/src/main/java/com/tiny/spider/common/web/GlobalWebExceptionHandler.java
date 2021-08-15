package com.tiny.spider.common.web;

import cn.hutool.core.util.StrUtil;
import com.tiny.spider.common.exception.BusinessException;
import com.tiny.spider.common.message.InterfaceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Formatter;
import java.util.stream.Collectors;

/**
 * @author zlf
 * @date 25 16:00
 * @version:1.0
 */
@ControllerAdvice
public class GlobalWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    /**
     * 方法参数错误
     */
    public RestfulResponseBody<Object> methodArgumentTypeError(MethodArgumentTypeMismatchException e) {

        String requestType = "";
        String message = "";
        String parameter = e.getParameter().getParameterName();
        int parameterIndex = e.getParameter().getParameterIndex();

        if (null != e.getRequiredType()) {
            requestType = e.getRequiredType().getTypeName();
        }

        try (Formatter formatter = new Formatter()) {
            formatter.format(" Parameter named '%s '(parameter index:%d), and the expected value type is %s ",
                    parameter, parameterIndex, requestType);
            message = formatter.toString();
        }

        if (logger.isErrorEnabled()) {
            logger.error("Method argument type error:", e);
        }

        return RestfulResponseUtils.errorResponse(InterfaceMessage.illegalSignature(message));

    }

    //MissingServletRequestParameterException

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public RestfulResponseBody<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return RestfulResponseUtils.errorResponse(InterfaceMessage.illegalSignature(e.getMessage()));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public RestfulResponseBody<Object> constraintViolationExceptionHandler(ConstraintViolationException e) {

        String message =
                e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());

        return RestfulResponseUtils.errorResponse(InterfaceMessage.illegalSignature(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RestfulResponseBody<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = "";

        if (null != e.getBindingResult().getFieldError()) {
            message = e.getBindingResult().getFieldError().getDefaultMessage();
        }

        if (StrUtil.isEmpty(message)) {
            message = e.getBindingResult().getFieldError().getField();
        }

        return RestfulResponseUtils.errorResponse(InterfaceMessage.illegalSignature(message));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RestfulResponseBody<Object> unknownError(Exception e) {

        if (logger.isErrorEnabled()) {
            logger.error("An unknown exception has been generated : ", e);
        }

        return RestfulResponseUtils.errorResponse(InterfaceMessage.illegalSignature(e.toString()));
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public RestfulResponseBody<Object> businessException(BusinessException e) {
        if (logger.isErrorEnabled()) {
            logger.error("An business exception has been generated : {}", e.getMessage());
        }
        return RestfulResponseUtils.errorResponse(InterfaceMessage.illegalSignature(e.getMessage()));
    }
}
