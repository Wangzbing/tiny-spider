package com.tiny.cloud.bookspider.listener;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.tiny.cloud.bookspider.event.query.ContentEvent;
import com.tiny.cloud.bookspider.event.query.InfoQueryEvent;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.spider.InfoSpider;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

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
        //寻找对应小说的章节，章节url
        Spider.create(infoSpider).addUrl(HOST+"/search_"+source.getBookName()+".html").run();
        //便利每个章节的地址，
    }





}
