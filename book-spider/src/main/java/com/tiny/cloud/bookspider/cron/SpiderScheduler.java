package com.tiny.cloud.bookspider.cron;

import com.tiny.cloud.bookspider.sevice.SpiderService;
import com.tiny.cloud.spider.common.strategy.SpiderContext;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
@Component
public class SpiderScheduler {
    @Resource
    SpiderContext spiderContext;


    @Scheduled(initialDelay = 1L,fixedRate = 86400000L)
    public void saveInfo(){
        SpiderService service = (SpiderService) spiderContext.getService(SpiderStore.QI_DIAN);
        service.saveInfo();
    }

    @Scheduled(initialDelay = 60000L,fixedRate = 10800000L)
    public void saveContent(){
        SpiderService service = (SpiderService) spiderContext.getService(SpiderStore.QI_DIAN);
        service.saveContent();
    }
}
