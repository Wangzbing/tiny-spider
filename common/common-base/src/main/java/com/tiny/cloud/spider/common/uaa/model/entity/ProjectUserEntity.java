package com.tiny.cloud.spider.common.uaa.model.entity;


import com.tiny.cloud.spider.common.orm.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/04/08
 */
@Entity
@Table(name = "user")
public class ProjectUserEntity extends BaseEntity {
    private Long userId;
    private String userName;
    private String loginName;
    private String password;
    private Date lastLoginTime;

    @Id
    @Column(name = "user_Id", nullable = false)
    public Long getUserId() {
        return userId;
    }

    public ProjectUserEntity setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    @Basic
    @Column(name = "user_Name", nullable = false)
    public String getUserName() {
        return userName;
    }

    public ProjectUserEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Basic
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public ProjectUserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    @Basic
    @Column(name = "login_Name", nullable = false)
    public String getLoginName() {
        return loginName;
    }

    public ProjectUserEntity setLoginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    @Basic
    @Column(name = "last_Login_Time", nullable = false)
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public ProjectUserEntity setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }
}
