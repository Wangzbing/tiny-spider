package com.tiny.cloud.bookspider.listener;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.tiny.cloud.bookspider.event.query.InfoQueryEvent;
import com.tiny.cloud.bookspider.model.entity.BookCategory;
import com.tiny.cloud.bookspider.model.entity.CateOrder;
import com.tiny.cloud.bookspider.model.entity.Spider;
import com.tiny.cloud.bookspider.model.repository.BookCategoryRepository;
import com.tiny.cloud.bookspider.model.repository.BookInfoRepository;
import com.tiny.cloud.bookspider.model.repository.CateOrderRepository;
import com.tiny.cloud.bookspider.model.repository.SpiderRepository;
import com.tiny.cloud.bookspider.spider.BookSpider;
import com.tiny.cloud.spider.common.snowflake.IDGenerator;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;
import com.tiny.cloud.spider.common.thread.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
@Component
@Slf4j
public class Listener {
    @Resource
    BookSpider bookSpider;
    @Resource
    BookInfoRepository repository;

    @Resource
    BookCategoryRepository categoryRepository;

    @Resource
    CateOrderRepository cateOrderRepository;

    @Resource
    IDGenerator idGenerator;

    @Resource
    SpiderRepository spiderRepository;

    private static final String INFO_ALL="/all";

    @Resource
    private ThreadPoolManager poolManager;

    private ThreadPoolExecutor pool;

    @PostConstruct
    public void init() {
        pool = poolManager.createThreadPool(Listener.class.getName());
    }

    @EventListener(classes = InfoQueryEvent.class)
    @Async
    public void queryAll(ApplicationEvent applicationEvent) throws InterruptedException {
        InfoQueryEvent infoQueryEvent=(InfoQueryEvent)applicationEvent;
        SpiderStore source = (SpiderStore) infoQueryEvent.getSource();
        List<BookCategory> allCategories = categoryRepository.findAll();
        Map<Long, String> collect = allCategories.stream().filter(s -> s.getParentCateId() == -1).map(s -> {
            StringBuilder sb = new StringBuilder();
            String url = source.getBaseUrl();
            sb.append(url);
            if (source.equals(SpiderStore.QI_DIAN)) {
                sb.append(INFO_ALL);
                if (CharSequenceUtil.isNotEmpty(s.getCateUrl())) {
                    sb.append(s.getCateUrl());
                }
            }
            return new Pair<>(s.getCateId(), sb.toString());
        }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        List<String> strings = allCategories.stream().filter(s -> s.getParentCateId() != -1).map(s -> {
            String s1 = collect.get(s.getParentCateId());
            return s1+s.getCateUrl() ;
        }).collect(Collectors.toList());
        strings.addAll(collect.values());
        List<CateOrder> cateOrders = cateOrderRepository.findAll();
        //各个榜单
        List<String> collect1 = cateOrders.stream().map(s -> {
            List<String> xArray = new ArrayList<>();
            for (String string : strings) {
                if (CharSequenceUtil.isNotEmpty(s.getOrderCode())) {
                    String s1 = StringUtils.substringAfterLast(string, "/");
                    if ("all".equals(s1)){
                        xArray.add(string+s.getOrderCode());
                    }else {
                        xArray.add(string + "-" + s.getOrderCode().substring(1));
                    }
                }else {
                    xArray.add(string);
                }
            }
            return xArray;
        }).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        //每页
        List<String> collect2 = IntStream.range(1, 5).parallel().mapToObj(s -> {
            List<String> strings1 = new ArrayList<>();
            for (String s1 : collect1) {
                String bb = StringUtils.substringAfterLast(s1, "/");
                if ("all".equals(bb)){
                    strings1.add(s1 + "/page" + s);
                }else {
                    strings1.add(s1 + "-page" + s);
                }
            }
            return strings1;
        }).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        List<Spider> spiders = collect2.stream().map(s -> {
            Spider spider = new Spider();
            spider.setId(idGenerator.nextId());
            spider.setName("ALL");
            spider.setUrl(s);
            spider.setStore(source.name());
            spider.setSuccess(Boolean.FALSE);
            spider.setStatus(0);
            return spider;
        }).collect(Collectors.toList());
        spiderRepository.saveAll(spiders);
        Map<String, Long> map = spiders.stream().collect(Collectors.toMap(Spider::getUrl, Spider::getId));
        collect2.forEach(s->{
            try {
                getAllInfo(s);
                flush(map.get(s));
            }catch (Exception e){
                log.warn("获取详情时发生异常",e);
            }
        });
    }

    public void flush(Long id) {
        Optional<Spider> byId = spiderRepository.findById(id);
        if (byId.isPresent()) {
            Spider spider = byId.get();
            spider.setStatus(1);
            spider.setSuccess(Boolean.TRUE);
            spiderRepository.save(spider);
        }
    }

    public void getAllInfo(String s) {
        us.codecraft.webmagic.Spider.create(bookSpider).addUrl(s).run();
    }
}
