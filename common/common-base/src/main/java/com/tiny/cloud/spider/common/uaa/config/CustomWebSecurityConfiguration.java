package com.tiny.cloud.spider.common.uaa.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Amin
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@ConditionalOnProperty(prefix = "tiny.security", name = "enable", havingValue = "true")
@Import({CustomAuthenticationConfiguration.class,CustomAuthenticationEntryPoint.class,CustomAuthenticationFailureHandler.class,
        CustomAuthenticationProvider.class,CustomAuthenticationSuccessHandler.class,CustomAuthenticationFilter.class,
        JwtToken.class
})
public class CustomWebSecurityConfiguration  extends WebSecurityConfigurerAdapter{

    @Resource
    private CustomAuthenticationProvider authenticationProvider;

    @Resource
    private CustomAuthenticationFilter customAuthenticationFilter;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/doc.html",
                "/uaa/**",
                "/resources/**",
                "/css/**",
                "/static/**",
                "fonts/**",
                "/images/**",
                "/js/**",
                "/webjars/**",
                "/favicon.ico",
                "/swagger-resources/**",
                "/v2/**",
                "/index",
                "/",
                "/favicon.ico"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
        http.csrf().disable();
        http.addFilterBefore(customAuthenticationFilter, SecurityContextPersistenceFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // disable page caching
        http.headers().disable();
        http.logout().logoutUrl("/logout").addLogoutHandler((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletRequest.getSession().removeAttribute("auth");
            try {
                httpServletResponse.sendRedirect("/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).invalidateHttpSession(true).deleteCookies("token");
    }

    @Bean
    @Override
    protected  AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }
}
