package com.tiny.cloud.bookspider.sevice.impl;

import com.tiny.cloud.bookspider.event.query.ContentEvent;
import com.tiny.cloud.bookspider.event.query.InfoQueryEvent;
import com.tiny.cloud.bookspider.model.entity.BookInfo;
import com.tiny.cloud.bookspider.model.repository.BookChapterRepository;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.publish.Publisher;
import com.tiny.cloud.bookspider.sevice.SpiderService;
import com.tiny.cloud.spider.common.strategy.annotations.MessageHandler;
import com.tiny.cloud.spider.common.strategy.enums.SpiderCategory;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    BookChapterRepository chapterRepository;
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
    public void saveContent(){
        List<BookInfo> all = repository.findAll();
        all.forEach(s->{
            ContentEvent contentEvent = new ContentEvent(s);
            publisher.publishEvent(contentEvent);
        });
    }
}
