package com.tiny.cloud.spider.common.uaa.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/04/07
 */
@ConfigurationProperties(prefix = "tiny.jwt")
@Data
public class JwtProp {
    private String jwtPre="tiny";
    private Long expire=3600L;
}
