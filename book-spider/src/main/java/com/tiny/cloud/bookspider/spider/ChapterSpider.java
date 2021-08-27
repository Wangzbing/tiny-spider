package com.tiny.cloud.bookspider.spider;

import cn.hutool.core.text.CharSequenceUtil;
import com.tiny.cloud.bookspider.model.entity.BookChapter;
import com.tiny.cloud.bookspider.model.repository.BookChapterRepository;
import com.tiny.cloud.bookspider.spider.model.SpiderBO;
import com.tiny.cloud.spider.common.base.StringPool;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangzb
 * @date 2021/8/18
 * @description
 */
@Component
public  class ChapterSpider extends BaseSpider<SpiderBO> implements PageProcessor {

    @Resource
    ContentSpider contentSpider;

    @Resource
    BookChapterRepository chapterRepository;

    private final Site site = Site.me().setRetryTimes(1).setSleepTime(10).setTimeOut(20000).setCharset("UTF-8");

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
            String text = s.select("a").text();
            if (CharSequenceUtil.isNotEmpty(href)){
                urls.put(atomicInteger.incrementAndGet(),href+ StringPool.SPLITTER+text);
            }
        });
        List<Map.Entry<Integer, String>> toSort = new ArrayList<>(urls.entrySet());
        toSort.sort(Map.Entry.comparingByKey());
        for (Map.Entry<Integer, String> s : toSort) {
            String[] split = s.getValue().split(StringPool.SPLITTER);
            getKeys().setOrder(s.getKey().longValue());
            getKeys().setUrl(split[1]);
            Optional<BookChapter> byOption = chapterRepository.findByOption(getKeys().getId(), s.getKey().longValue());
            if (!byOption.isPresent()) {
                contentSpider.setKeys(getKeys());
                Spider.create(contentSpider).addUrl(page.getUrl() + split[0]).thread(5).run();
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}


