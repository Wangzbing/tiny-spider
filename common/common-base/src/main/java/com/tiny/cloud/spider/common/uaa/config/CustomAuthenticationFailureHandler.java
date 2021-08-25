package com.tiny.cloud.spider.common.uaa.config;

import cn.hutool.json.JSONUtil;
import com.tiny.cloud.spider.common.message.SecurityMessage;
import com.tiny.cloud.spider.common.web.RestfulResponseBody;
import com.tiny.cloud.spider.common.web.RestfulResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/25
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        RestfulResponseBody<Object> responseBody = RestfulResponseUtils.errorResponse(SecurityMessage.AuthFail(e.getMessage()));
        String s = JSONUtil.toJsonStr(responseBody);
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(s);
    }
}
