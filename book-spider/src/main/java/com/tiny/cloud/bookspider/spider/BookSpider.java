package com.tiny.cloud.bookspider.spider;

import com.tiny.cloud.bookspider.model.entity.BookAuthor;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.repository.BookAuthorRepository;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.sevice.SpiderService;
import com.tiny.cloud.spider.common.snowflake.IDGenerator;
import com.tiny.cloud.spider.common.strategy.SpiderContext;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author wangzb
 * @date 2021/8/17
 * @description
 */
@Component
public class BookSpider implements PageProcessor {

    @Resource
    BookInfoRepository infoRepository;

    @Resource
    BookAuthorRepository bookAuthorRepository;

    @Resource
    IDGenerator idGenerator;

    private final Site site = Site.me().setRetryTimes(1).setSleepTime(1000).setCharset("UTF-8");

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document parse = Jsoup.parse(html.get());
        parse.select("ul").stream().filter(s->s.hasClass("all-img-list")).findFirst().ifPresent(s->{
            Elements lis = s.select("li");
            lis.forEach(each->{
                BookInfo bookInfo = new BookInfo();
                //小说图片
                String img = each.getElementsByClass("book-img-box").select("img").attr("src");
                bookInfo.setBookImage(img);
                // 小说地址
                String realUrl = each.getElementsByClass("book-img-box").select("a").attr("href");
                String bookId = StringUtils.substringAfterLast(realUrl, "/");
                bookInfo.setBookOriginId(Long.parseLong(bookId));
                bookInfo.setBookInfoIntro(realUrl);
                Elements infos = each.getElementsByClass("book-mid-info");
                //小说名称
                String title = infos.select("h4").text();
                bookInfo.setBookName(title);
                Optional<Element> first = infos.select("p a").stream().filter(xx -> xx.hasClass("name")).findFirst();
                if (first.isPresent()) {
                    BookAuthor bookAuthor = new BookAuthor();
                    long authorId = idGenerator.nextId();
                    Element element = first.get();
                    //作家id
                    String s1 = StringUtils.substringAfterLast(element.attr("href"), "/");
                    //作家名称
                    String text = element.text();
                    bookAuthor.setAuthorId(authorId);
                    bookAuthor.setAuthorOriginId(Long.parseLong(s1));
                    bookAuthor.setAuthorName(text);
                    BookAuthor byAuthorOriginId = bookAuthorRepository.findByAuthorOriginId(Long.parseLong(s1));
                    if (byAuthorOriginId==null){
                        bookAuthorRepository.save(bookAuthor);
                        bookInfo.setBookAuthorId(authorId);
                        bookInfo.setBookAuthorName(text);
                    }else{
                        Long authorId1 = byAuthorOriginId.getAuthorId();
                        bookInfo.setBookAuthorId(authorId1);
                        bookInfo.setBookAuthorName(text);
                    }
                }
                infos.select("p").stream().filter(yy->yy.hasClass("intro")).findFirst().ifPresent(yyy->{
                    String intro = yyy.text();
                    bookInfo.setBookIntro(intro);
                });
                bookInfo.setBookId(idGenerator.nextId());
                BookInfo bookOrigin = infoRepository.findByBookOriginId(bookInfo.getBookOriginId());
                if (bookOrigin==null){
                    infoRepository.save(bookInfo);
                }
            });
        });
    }

    @Override
    public Site getSite() {
        return site;
    }
}
