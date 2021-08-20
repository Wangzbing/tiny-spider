package com.tiny.cloud.bookspider.spider;

import cn.hutool.core.lang.Pair;
import com.github.benmanes.caffeine.cache.Cache;
import com.tiny.cloud.bookspider.model.entity.BookChapter;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.repository.BookChapterRepository;
import com.tiny.cloud.bookspider.spider.model.SpiderBO;
import com.tiny.cloud.spider.common.snowflake.IDGenerator;
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
import java.util.function.Function;

/**
 * @author wangzb
 * @date 2021/8/18
 * @description
 */
@Component
public class InfoSpider extends BaseSpider<SpiderBO> implements PageProcessor {
    @Resource
    ChapterSpider chapterSpider;

    private final Site site = Site.me().setRetryTimes(1).setSleepTime(1000).setCharset("UTF-8");

    private static final String HOST="https://www.yanqingshu.com";

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document parse = Jsoup.parse(html.get());
        Elements bookbox = parse.getElementsByClass("bookbox");
        if (bookbox!=null&&!bookbox.isEmpty()){
            Element element = bookbox.get(0);
            String attr = element.getElementsByClass("bookname").select("a").attr("href");
            String text = element.getElementsByClass("bookname").select("a").text();
            if (text.equals(getKeys().getUrl())){
                chapterSpider.setKeys(getKeys());
                Spider.create(chapterSpider).addUrl(HOST+attr).run();
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
