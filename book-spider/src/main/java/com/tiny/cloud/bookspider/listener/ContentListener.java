package com.tiny.cloud.bookspider.listener;

import cn.hutool.core.lang.Pair;
import com.github.benmanes.caffeine.cache.Cache;
import com.tiny.cloud.bookspider.event.query.ContentEvent;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.spider.InfoSpider;
import com.tiny.cloud.bookspider.spider.model.SpiderBO;
import com.tiny.cloud.spider.common.base.StringPool;
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
    @Resource
    InfoSpider infoSpider;

    private static final String HOST="https://www.yanqingshu.com";

    @EventListener(classes = ContentEvent.class)
    @Async
    public void queryAll(ApplicationEvent applicationEvent) throws InterruptedException {
        ContentEvent infoQueryEvent=(ContentEvent)applicationEvent;
        BookInfo source = (BookInfo) infoQueryEvent.getSource();
        infoSpider.setKeys(new SpiderBO().setId(source.getBookId()).setUrl(source.getBookName()));
        //寻找对应小说的章节，章节url
        Spider.create(infoSpider).addUrl(HOST+"/search_"+source.getBookName()+".html").run();
        //便利每个章节的地址，
    }


}
