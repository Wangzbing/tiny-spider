package com.tiny.cloud.bookspider.listener;

import cn.hutool.core.lang.Pair;
import com.github.benmanes.caffeine.cache.Cache;
import com.tiny.cloud.bookspider.event.query.ContentEvent;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.spider.InfoSpider;
import com.tiny.cloud.bookspider.spider.model.SpiderBO;
import com.tiny.cloud.common.config.XxlJobAutoConfig;
import com.tiny.cloud.spider.common.base.StringPool;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;

/**
 * @author wangzb
 * @date 2021/8/18
 * @description
 */
@Component
@Slf4j
public class ContentListener {
    @Resource
    BookInfoRepository infoRepository;


    private static final String HOST="https://www.yanqingshu.com";

    @Async
    public void queryAll(ApplicationEvent applicationEvent){
        ContentEvent infoQueryEvent=(ContentEvent)applicationEvent;
        BookInfo source = (BookInfo) infoQueryEvent.getSource();
        XxlJobHelper.log("开始爬取章节内容{}",source.getBookName());
        //便利每个章节的地址，
    }


}
