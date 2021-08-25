package com.tiny.cloud.spider.common.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/25
 */
public class CustomAuthenticationFilter  extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_USER_NAME = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD = "password";

    public CustomAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Nullable
    protected String obtainName(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_USER_NAME);
    }
    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_PASSWORD);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (!RequestMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " +request.getMethod());
        }
        String id = request.getSession().getId();
        String name = obtainName(request);
        String password = obtainPassword(request);
        LoginFormVO loginFormVO = new LoginFormVO(null,name);
        CustomAuthenticationToken authRequest = new CustomAuthenticationToken(password, loginFormVO);
        // Allow subclasses to set the "details" property
        authRequest.setDetails(id);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Resource
    private void authenticationManager(AuthenticationManager authenticationManager){
        super.setAuthenticationManager(authenticationManager);
    }
}
