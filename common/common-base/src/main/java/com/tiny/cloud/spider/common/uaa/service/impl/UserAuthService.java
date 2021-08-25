package com.tiny.cloud.spider.common.uaa.service.impl;

import com.tiny.cloud.spider.common.uaa.model.entity.ProjectUserEntity;
import com.tiny.cloud.spider.common.uaa.model.repository.ProjectUserRepository;
import com.tiny.cloud.spider.common.uaa.vos.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/24
 */
@Service
public class UserAuthService implements UserDetailsService {
    @Resource
    PasswordEncoder encoder;

    @Resource
    ProjectUserRepository repository;
    /**
     * 查询名称
     * @param name 名称
     * @return vo
     */
    public AccountVO getAccountByName(String name){
        ProjectUserEntity account = repository.getAccountByName(name);
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account,accountVO);
        return accountVO;
    }

    @Override
    public UserDetails loadUserByUsername(String name){
        ProjectUserEntity entity = repository.getAccountByName(name);
        return User.withUsername(entity.getUserName()).password(entity.getPassword()).authorities(AuthorityUtils.NO_AUTHORITIES).build();
    }
}
