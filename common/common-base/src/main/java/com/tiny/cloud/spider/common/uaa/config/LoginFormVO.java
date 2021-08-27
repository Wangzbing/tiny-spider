package com.tiny.cloud.spider.common.uaa.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/25
 */
@Data
@AllArgsConstructor
public class LoginFormVO {
    private String token;
    private String username;
    private Long userId;
}
