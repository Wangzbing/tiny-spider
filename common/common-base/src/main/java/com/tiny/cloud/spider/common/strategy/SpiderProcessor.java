package com.tiny.cloud.spider.common.strategy;

import com.tiny.cloud.spider.common.strategy.annotations.MessageHandler;
import com.tiny.cloud.spider.common.strategy.enums.SpiderCategory;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wangzb
 * @date 2021/6/10
 * @description
 */
@Component
public class SpiderProcessor implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, Object> messageHandler = applicationContext.getBeansWithAnnotation(MessageHandler.class);
        messageHandler.forEach((k, v) -> {
            MessageHandler annotation = v.getClass().getAnnotation(MessageHandler.class);
            SpiderStore store = annotation.store();
            SpiderCategory category = annotation.category();
            SpiderCategory category1 = store.getCategory();
            if (category.equals(category1)){
                SpiderContext.SERVICES.put(store, v.getClass());
            }
        });
    }
}
