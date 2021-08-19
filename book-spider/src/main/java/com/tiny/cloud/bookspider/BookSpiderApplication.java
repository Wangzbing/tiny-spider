package com.tiny.cloud.bookspider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BookSpiderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookSpiderApplication.class,args);
    }
}
