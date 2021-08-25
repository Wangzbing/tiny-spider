package com.tiny.cloud.spider.common.uaa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateCodeResultVO {
    private String img;
    private Long sessionId;
}
