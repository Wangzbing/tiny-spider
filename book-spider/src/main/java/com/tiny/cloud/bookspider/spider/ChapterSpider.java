package com.tiny.cloud.bookspider.spider;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangzb
 * @date 2021/8/18
 * @description
 */
@Component
public  class ChapterSpider implements PageProcessor {

    @Resource
    ContentSpider contentSpider;

    private final Site site = Site.me().setRetryTimes(1).setSleepTime(1000).setCharset("UTF-8");
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document parse = Jsoup.parse(html.get());
        Elements allChapter = parse.select("dl");
        Element element = allChapter.get(0);
        Elements elements = new Elements();
        int j=0;
        for (int i = 0; i < element.children().size(); i++) {
            if (element.children().get(i).text().contains("章节目录")) {
                j=1;
            }
            if (j>0){
                elements.add(element.children().get(i));
            }
        }
        Map<Integer,String> urls = new HashMap<>(elements.size());
        AtomicInteger atomicInteger = new AtomicInteger();
        elements.forEach(s-> {
            String href = s.select("a").attr("href");
            if (StrUtil.isNotEmpty(href)){
                urls.put(atomicInteger.incrementAndGet(),href);
            }
        });
        urls.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(s-> Spider.create(contentSpider).addUrl(page.getUrl()+s.getValue()).thread(2).run());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
