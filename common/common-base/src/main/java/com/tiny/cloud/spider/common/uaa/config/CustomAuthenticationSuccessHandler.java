package com.tiny.cloud.spider.common.uaa.config;

import cn.hutool.json.JSONUtil;
import com.tiny.cloud.spider.common.web.RestfulResponseBody;
import com.tiny.cloud.spider.common.web.RestfulResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        LoginFormVO token = (LoginFormVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RestfulResponseBody<String> responseBody = RestfulResponseUtils.successResponse(token.getToken(), "登录成功");
        String s = JSONUtil.toJsonStr(responseBody);
        httpServletRequest.getSession().setAttribute("auth",token.getToken());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(s);
    }
}
