package com.tiny.cloud.spider.common.web.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/04/06
 */
@ConfigurationProperties("tiny.web.cors")
@Configuration
public class WebCorsConfiguration {

    private CorsConfiguration config = new CorsConfiguration();

    public CorsConfiguration getConfig() {
        return config;
    }

    public void setConfig(CorsConfiguration config) {
        this.config = config;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
