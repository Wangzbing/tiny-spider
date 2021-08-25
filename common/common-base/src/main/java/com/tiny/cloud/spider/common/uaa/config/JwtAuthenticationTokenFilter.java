package com.tiny.cloud.spider.common.uaa.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.tiny.cloud.spider.common.uaa.service.impl.UserAuthService;
import com.tiny.cloud.spider.common.uaa.vos.AccountVO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/03/01
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    JwtToken jwtToken;

    @Resource
    private UserAuthService userAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> token = Arrays.stream(cookies).filter(s -> "token".equals(s.getName())).findAny();
        if (token.isPresent()){
            authorization=token.get().getValue();
        }
        String auth =(String) request.getSession().getAttribute("auth");
        authorization= CharSequenceUtil.isEmpty(authorization)?auth:authorization;
        String username = jwtToken.getUsernameFromToken(authorization);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            AccountVO account = userAuthService.getAccountByName(username);
            UserDetails userDetails = userAuthService.loadUserByUsername(username);
            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (Boolean.TRUE.equals(jwtToken.validateToken(authorization, account))) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(account);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
