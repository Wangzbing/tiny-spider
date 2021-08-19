package com.tiny.cloud.spider.common.strategy;

import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangzb
 * @date 2021/6/10
 * @description
 */
@Component
public class SpiderContext {
    @Resource
    private ApplicationContext applicationContext;

    public static final Map<SpiderStore, Class<?>> SERVICES = new ConcurrentHashMap<>(4);

    public Object getService(SpiderStore types) {
        Class<?> serviceClass = SERVICES.get(types);
        if (serviceClass == null) {
            throw new IllegalArgumentException();
        }
        return applicationContext.getBean(serviceClass);
    }
}
