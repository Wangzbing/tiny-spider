package com.tiny.cloud.common.config.xxl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangzb
 * @date 2021/8/30
 * @description
 */
@Data
@ConfigurationProperties("xxl.job")
public class XxlJobProp {
    private AdminProp admin;
    private String accessToken;
    private XxlJobExecutorProp executor;
}
