package com.tiny.cloud.bookspider.cron;

import com.tiny.cloud.bookspider.sevice.SpiderService;
import com.tiny.cloud.spider.common.strategy.SpiderContext;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
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


    @XxlJob("info")
    public void saveInfo(){
        SpiderService service = (SpiderService) spiderContext.getService(SpiderStore.QI_DIAN);
        service.saveInfo();
    }

    @XxlJob("content")
    public void saveContent(){
        String jobParam = XxlJobHelper.getJobParam();
        SpiderService service = (SpiderService) spiderContext.getService(SpiderStore.QI_DIAN);
        service.saveContent(jobParam);
        XxlJobHelper.handleSuccess();
    }
}
