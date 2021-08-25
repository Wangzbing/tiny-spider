package com.tiny.cloud.spider.common.uaa.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.tiny.cloud.spider.common.uaa.service.impl.UserAuthService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.security.auth.login.AccountExpiredException;
import java.util.ArrayList;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/25
 */
@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {
    @Resource
    private JwtToken jwtToken;

    @Resource
    private UserAuthService userAuthService;

    @Resource
    private PasswordEncoder passwordEncoder;

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication){
        CustomAuthenticationToken unAuthenticationToken = (CustomAuthenticationToken) authentication;
        LoginFormVO principal = (LoginFormVO)unAuthenticationToken.getPrincipal();
        if (principal==null){
            throw new AccountExpiredException("登录错误");
        }
        String password=(String)authentication.getCredentials();
        // base64 解密
        UserDetails userDetails = userAuthService.loadUserByUsername(principal.getUsername());
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("用户名密码不正确，请重新登陆！");
        }
        String jwt = jwtToken.generateToken(principal.getUsername());
        // 重新刷新登录时间
        principal.setToken(jwt);
        return getAuth(password, principal);
    }

    private Authentication getAuth(String details,LoginFormVO principal) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ACCOUNT"));
        return new CustomAuthenticationToken(authorities,details, principal);
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return CustomAuthenticationToken.class.isAssignableFrom(aClass);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
