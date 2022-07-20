package com.tiny.cloud.bookspider.sevice.impl;

import com.tiny.cloud.bookspider.event.query.InfoQueryEvent;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.publish.Publisher;
import com.tiny.cloud.bookspider.sevice.SpiderService;
import com.tiny.cloud.common.utils.OkHttpUtils;
import com.tiny.cloud.spider.common.strategy.annotations.MessageHandler;
import com.tiny.cloud.spider.common.strategy.enums.SpiderCategory;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import com.tiny.cloud.spider.common.thread.ThreadPoolManager;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
@Service
@MessageHandler(store = SpiderStore.QI_DIAN,category = SpiderCategory.BOOK)
public class BookInfoSpiderServiceImpl implements SpiderService {

    @Resource
    Publisher publisher;

    @Resource
    BookInfoRepository repository;

    @Resource
    ThreadPoolManager poolManager;

    public static ExecutorService pool;

    private static final String HOST="https://www.yanqingshu.com";
    /**
     * 保存详情
     */
    @Override
    public void saveInfo() {
        //查找所有的url
        InfoQueryEvent infoQueryEvent = new InfoQueryEvent(SpiderStore.QI_DIAN);
        publisher.publishEvent(infoQueryEvent);
    }

    /**
     * 保存排行
     */
    @Override
    public void saveRank() {
        System.out.println("rank");
    }

    @Override
    public void saveContent(String jobParam){
        //线程
        repository.findAll().forEach(s-> CompletableFuture.supplyAsync(()->save(s),pool));
    }

    private CompletableFuture<?> save(BookInfo s) {
        try {
            Response data = OkHttpUtils.getInstance().getData(HOST + "/search_" + s.getBookName() + ".html");
            if (data.isSuccessful()) {
                if (data.body() != null) {
                    String html = data.body().string();

                } else {
                    addContentFail(s.getBookId(), s.getBookName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Async
    public void addContentFail(Long bookId, String bookName) {
        
    }
}
