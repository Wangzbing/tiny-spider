package com.tiny.cloud.spider.common.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/25
 */
@Configuration
public class CustomAuthenticationConfiguration {
    @Resource
    private CustomAuthenticationFailureHandler failureHandler;
    @Resource
    private CustomAuthenticationSuccessHandler successHandler;
    @Resource
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean(name = {
            "customAuthenticationFilter"
    })
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter();
        // 配置 authenticationManager
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(customAuthenticationProvider));
        authenticationFilter.setAuthenticationManager(providerManager);
        // 成功处理器
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        // 失败处理器
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return authenticationFilter;
    }
    @Bean(name = {
            "jwtAuthenticationTokenFilter"
    })
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean(name = {
            "passwordEncoder"
    })
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
