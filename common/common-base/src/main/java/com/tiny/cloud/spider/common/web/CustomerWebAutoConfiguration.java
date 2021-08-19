package com.tiny.cloud.spider.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/19
 */
@Import({
        RestfulResponseBodyAdvice.class,
        GlobalWebExceptionHandler.class
})
@Configuration
public class CustomerWebAutoConfiguration {
}
