package com.tiny.cloud.bookspider.spider;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangzb
 * @date 2021/8/19
 * @description
 */
@Getter
@Setter
public class BaseSpider<T> {
    private T keys;
}
