package com.tiny.cloud.bookspider.publish;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
@Component
public class Publisher implements ApplicationContextAware {
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public void publishEvent(ApplicationEvent event){
        applicationContext.publishEvent(event);
    }
}
