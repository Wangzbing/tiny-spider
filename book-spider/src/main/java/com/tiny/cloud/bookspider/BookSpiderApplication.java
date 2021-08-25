package com.tiny.cloud.bookspider;

import com.tiny.cloud.spider.common.uaa.config.CustomWebSecurityConfiguration;
import com.tiny.cloud.spider.common.uaa.service.impl.UserAuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
@SpringBootApplication(exclude = {CustomWebSecurityConfiguration.class, UserAuthService.class})
@EnableAsync
@EnableScheduling
public class BookSpiderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookSpiderApplication.class,args);
    }
}
