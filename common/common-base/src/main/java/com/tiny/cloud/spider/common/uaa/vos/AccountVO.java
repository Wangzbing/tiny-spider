package com.tiny.cloud.spider.common.uaa.vos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/24
 */
@Data
@Accessors(chain = true)
public class AccountVO {
    private Long userId;
    private String userName;
    private String loginName;
}
