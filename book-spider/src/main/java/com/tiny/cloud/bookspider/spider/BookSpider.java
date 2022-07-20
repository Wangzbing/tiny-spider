package com.tiny.cloud.bookspider.spider;

import cn.hutool.core.lang.Pair;
import com.tiny.cloud.bookspider.model.entity.BookAuthor;
import com.tiny.cloud.bookspider.model.entity.BookCategory;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.entity.RelInfoCategory;
import com.tiny.cloud.bookspider.model.repository.BookAuthorRepository;
import com.tiny.cloud.bookspider.model.repository.BookCategoryRepository;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.model.repository.RelInfoCategoryRepository;
import com.tiny.cloud.spider.common.snowflake.IDGenerator;
import com.xxl.job.core.context.XxlJobHelper;
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    BookCategoryRepository categoryRepository;

    @Resource
    RelInfoCategoryRepository relInfoCategoryRepository;

    @Resource
    IDGenerator idGenerator;

    private Map<String, Long> allCate = new ConcurrentHashMap<>(50);

    private final Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setCharset("UTF-8");

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document parse = Jsoup.parse(html.get());
        parse.select("ul").stream().filter(s->s.hasClass("all-img-list")).findFirst().ifPresent(s->{
            XxlJobHelper.log("开始爬取书籍详情");
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
                    if (byAuthorOriginId == null) {
                        bookAuthorRepository.save(bookAuthor);
                        bookInfo.setBookAuthorId(authorId);
                        bookInfo.setBookAuthorName(text);
                    } else {
                        Long authorId1 = byAuthorOriginId.getAuthorId();
                        bookInfo.setBookAuthorId(authorId1);
                        bookInfo.setBookAuthorName(text);
                    }
                }
                infos.select("p").stream().filter(yy->yy.hasClass("intro")).findFirst().ifPresent(yyy->{
                    String intro = yyy.text();
                    bookInfo.setBookIntro(intro);
                });
                long book = idGenerator.nextId();
                bookInfo.setBookId(book);
                BookInfo bookOrigin = infoRepository.findByBookOriginId(bookInfo.getBookOriginId());
                if (bookOrigin==null){
                    XxlJobHelper.log("爬取书籍结束 书籍名称为{}",bookInfo.getBookName());
                    infoRepository.save(bookInfo);
                }
                infos.select("p a").stream().filter(x->x.hasClass("go-sub-type")).findFirst().ifPresent(x->{
                    String attr = x.attr("data-subtypeid");
                    Long aLong = allCate.get(attr);
                    if (aLong!=null){
                        RelInfoCategory cateId = relInfoCategoryRepository.findByBookIdAndCateId(book, aLong);
                        if (cateId==null) {
                            long relId = idGenerator.nextId();
                            RelInfoCategory relInfoCategory = new RelInfoCategory();
                            relInfoCategory.setBookId(book);
                            relInfoCategory.setCateId(aLong);
                            relInfoCategory.setRelId(relId);
                            relInfoCategoryRepository.save(relInfoCategory);
                        }
                    }
                });
            });
        });
    }

    @Override
    public Site getSite() {
        return site;
    }

    @PostConstruct
    public Map<String,Long> getSub(){
        List<BookCategory> all = categoryRepository.findAll();
        all.stream().filter(x -> x.getParentCateId() != -1).forEach(s->{
            String substring = s.getCateUrl().substring(10);
            allCate.put(substring,s.getCateId());
        });
        return allCate;
    }
}
