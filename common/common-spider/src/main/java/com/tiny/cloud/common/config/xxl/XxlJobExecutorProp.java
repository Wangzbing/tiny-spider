package com.tiny.cloud.common.config.xxl;

import lombok.Data;

/**
 * @author wangzb
 * @date 2021/8/30
 * @description
 */
@Data
public class XxlJobExecutorProp {
    private String appName;
    private String address;
    private String ip;
    private Integer port;
    private String logpath;
    private Integer logretentiondays;
}
