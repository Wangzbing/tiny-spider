package com.tiny.cloud.bookspider.spider.model;

import cn.hutool.core.codec.Base64;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangzb
 * @date 2021/8/19
 * @description
 */
@Data
@Accessors(chain = true)
public class SpiderBO {
    private Long id;
    private String url;
    private Long order;
}
