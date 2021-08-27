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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
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
        //查找对应的获取对应的分类
        Map<Long, List<BookCategory>> all = allCategories.stream().collect(Collectors.groupingBy(BookCategory::getParentId));
        int size = all.size();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        all.forEach((key, value) -> pool.execute(() -> {
            try {
                Map<Long, String> collect = value.stream().filter(x -> x.getParentCateId() == -1).map(x -> {
                    StringBuilder sb = new StringBuilder();
                    String url = source.getBaseUrl();
                    sb.append(url);
                    if (source.equals(SpiderStore.QI_DIAN)) {
                        sb.append(x.getParentUrl());
                        if (CharSequenceUtil.isNotEmpty(x.getCateUrl())) {
                            sb.append(x.getCateUrl());
                        }
                    }
                    return new Pair<>(x.getCateId(), sb.toString());
                }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
                List<String> strings = allCategories.stream().filter(x -> x.getParentCateId() != -1).map(x -> {
                    String s1 = collect.get(x.getParentCateId());
                    return s1 + x.getCateUrl();
                }).collect(Collectors.toList());
                strings.addAll(collect.values());
                List<CateOrder> cateOrders = cateOrderRepository.findAll();
                //各个榜单
                List<String> collect1 = cateOrders.stream().map(x -> {
                    List<String> xArray = new ArrayList<>();
                    for (String string : strings) {
                        if (CharSequenceUtil.isNotEmpty(x.getOrderCode())) {
                            String s1 = StringUtils.substringAfterLast(string, "/");
                            if ("all".equals(s1)) {
                                xArray.add(string + x.getOrderCode());
                            } else {
                                xArray.add(string + "-" + x.getOrderCode().substring(1));
                            }
                        } else {
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
                        if ("all".equals(bb)) {
                            strings1.add(s1 + "/page" + s);
                        } else {
                            strings1.add(s1 + "-page" + s);
                        }
                    }
                    return strings1;
                }).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
                List<Spider> spiders = collect2.stream().map(x -> {
                    Spider spider = new Spider();
                    spider.setId(idGenerator.nextId());
                    spider.setName("ALL");
                    spider.setUrl(x);
                    spider.setStore(source.name());
                    spider.setSuccess(Boolean.FALSE);
                    spider.setStatus(0);
                    return spider;
                }).collect(Collectors.toList());
                spiderRepository.saveAll(spiders);
                Map<String, Long> map = spiders.stream().collect(Collectors.toMap(Spider::getUrl, Spider::getId));
                collect2.forEach(x -> {
                    try {
                        getAllInfo(x);
                        flush(map.get(x));
                    } catch (Exception e) {
                        log.warn("获取详情时发生异常", e);
                    }
                });
            } catch (Exception e) {
                log.warn("异常", e);
            } finally {
                countDownLatch.countDown();
            }
        }));
        countDownLatch.await();

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
