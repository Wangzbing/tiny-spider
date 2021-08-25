package com.tiny.cloud.spider.common;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wangzb
 * @date 2021/8/24
 * @description
 */
@Controller
public class ErrorPageConfig {
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
        return (s)->{
            ErrorPage[] errorPages=new ErrorPage[]{
                    new ErrorPage(HttpStatus.FORBIDDEN,"/403"),
                    new ErrorPage(HttpStatus.NOT_FOUND,"/404"),
                    new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/500")
            };
            s.setErrorPages(Arrays.stream(errorPages).collect(Collectors.toSet()));
        };
    }

    @GetMapping("/403")
    public String p403(){
        return "403";
    }

    @GetMapping("/500")
    public String p500(){
        return "500";
    }

    @GetMapping("/404")
    public String p404(){
        return "404";
    }

}
