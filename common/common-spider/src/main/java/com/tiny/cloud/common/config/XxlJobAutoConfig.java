package com.tiny.cloud.common.config;

import com.tiny.cloud.common.config.xxl.XxlJobProp;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangzb
 * @date 2021/8/30
 * @description
 */
@Configuration
@EnableConfigurationProperties(XxlJobProp.class)
public class XxlJobAutoConfig {
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProp xxlJobProp) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProp.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProp.getExecutor().getAppName());
        xxlJobSpringExecutor.setIp(xxlJobProp.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobProp.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProp.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProp.getExecutor().getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProp.getExecutor().getLogretentiondays());
        return xxlJobSpringExecutor;
    }
}
