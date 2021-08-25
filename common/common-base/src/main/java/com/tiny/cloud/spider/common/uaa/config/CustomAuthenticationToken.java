package com.tiny.cloud.spider.common.uaa.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/25
 */
public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private String password;
    private LoginFormVO loginFormVO;

    public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String password, LoginFormVO loginFormVO) {
        super(authorities);
        this.password = password;
        this.loginFormVO = loginFormVO;
        super.setAuthenticated(true);
    }

    public CustomAuthenticationToken(String password, LoginFormVO loginFormVO) {
        super(null);
        this.password = password;
        this.loginFormVO = loginFormVO;
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.loginFormVO;
    }
}
