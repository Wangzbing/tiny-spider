package com.tiny.cloud.bookspider.spider;

import com.tiny.cloud.bookspider.model.entity.BookChapter;
import com.tiny.cloud.bookspider.model.repository.BookChapterRepository;
import com.tiny.cloud.bookspider.spider.model.SpiderBO;
import com.tiny.cloud.common.utils.OkHttpUtils;
import com.tiny.cloud.spider.common.snowflake.IDGenerator;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wangzb
 * @date 2021/8/18
 * @description
 */
@Component
public class ContentSpider extends BaseSpider<SpiderBO> implements PageProcessor {

    @Resource
    private BookChapterRepository chapterRepository;

    @Resource
    IDGenerator generator;

    private final Site site = Site.me().setRetryTimes(1).setSleepTime(1000).setCharset("UTF-8");
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document parse = Jsoup.parse(html.get());
        parse.getElementsByTag("script").stream().filter(s->s.hasAttr("type")&&s.data().contains("bufurl")).findFirst().ifPresent(x->{
            String[] script = x.data().split("var");
            AtomicReference<String> string= new AtomicReference<>("");
            Arrays.stream(script).forEach(s->{
                boolean bufurl = s.contains("bufurl");
                if (bufurl) {
                    String[] split = s.split("=");
                    String s1 = split[1].split(";")[0].replace("'", "");
                    try {
                        Response data = OkHttpUtils.getInstance().getData("https://" + site.getDomain() + s1);
                        if (data.isSuccessful()){
                            string.set(data.body()!=null?data.body().string():"");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            String content = parse.getElementsByClass("showtxt").html()+string.get();
            BookChapter bookChapter = new BookChapter();
            bookChapter.setChapterId(generator.nextId());
            bookChapter.setBookId(getKeys().getId());
            bookChapter.setChapterName(getKeys().getUrl());
            bookChapter.setChapterOrder(getKeys().getOrder());
            bookChapter.setChapterUrl(page.getUrl().get());
            bookChapter.setContent(content);
            int length = content.replace("<br>", "").replace("/n","").length();
            bookChapter.setContentLength(Integer.toUnsignedLong(length));
            chapterRepository.save(bookChapter);
        });
    }

    @Override
    public Site getSite() {
        return site;
    }
}
