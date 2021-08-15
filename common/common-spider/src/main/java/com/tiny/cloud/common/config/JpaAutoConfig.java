package com.tiny.cloud.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author wangzb
 * @date 2021/7/20
 * @description
 */
@EntityScan(basePackages = "com.tiny.cloud.**.model.entity")
@Configuration
@EnableJpaRepositories(basePackages="com.tiny.cloud.*.model.repository")
public class JpaAutoConfig {

}
