package com.tiny.cloud.spider.common.uaa.model.repository;

import com.tiny.cloud.spider.common.uaa.model.entity.ProjectUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/04/08
 */
@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUserEntity,Long> {
    /**
     * 根据用户名查询用户
     * @param name name
     * @return Entity
     */
    @Query("from ProjectUserEntity  a where a.deleted=false and a.loginName=?1")
    ProjectUserEntity getAccountByName(String name);
}
