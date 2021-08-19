package com.tiny.cloud.bookspider.spider;

import com.tiny.cloud.common.utils.OkHttpUtils;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wangzb
 * @date 2021/8/18
 * @description
 */
@Component
public class ContentSpider implements PageProcessor {
    private final Site site = Site.me().setRetryTimes(1).setSleepTime(1000).setCharset("UTF-8");
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document parse = Jsoup.parse(html.get());
        String[] script = parse.getElementsByTag("script").stream().filter(s->s.hasAttr("type")&&s.data().contains("bufurl")).findFirst().get().data().split("var");
        AtomicReference<String> string= new AtomicReference<>("");
        Arrays.stream(script).forEach(s->{
            boolean bufurl = s.contains("bufurl");
            if (bufurl) {
                String[] split = s.split("=");
                String s1 = split[1].split(";")[0].replace("'", "");
                try {
                    Response data = OkHttpUtils.getInstance().getData("https://" + site.getDomain() + s1);
                    string.set(data.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String showtxt = parse.getElementsByClass("showtxt").html()+string.get();
        System.out.println(showtxt);
    }

    @Override
    public Site getSite() {
        return site;
    }
}
