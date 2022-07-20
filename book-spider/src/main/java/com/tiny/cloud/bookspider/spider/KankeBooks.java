package com.tiny.cloud.bookspider.spider;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import com.tiny.cloud.spider.common.thread.ThreadPoolManager;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhongbing.wang@xiaoxitech.com
 * @date 2022/7/12
 */
@Slf4j
public class KankeBooks {
    private static final ThreadPoolExecutor kanke;
    static {
        kanke= new ThreadPoolManager().createThreadPool("kanke");
    }
    public static void main(String[] args) {
        String baseUrl = "https://www.kankezw.com/du/81/81217/";
        String s = HttpUtil.get(baseUrl);
        Document parse = Jsoup.parse(s);
        Elements thisE = parse.getElementsByClass("pc_list");
        Element y = thisE.get(1);
        Elements ul_li = y.select("ul li a");
        AtomicInteger index = new AtomicInteger();
        List<Ca> href = ul_li.stream().map(x -> new Ca().setIndex(index.incrementAndGet()).setName(x.text()).setUrl(baseUrl + x.attr("href"))).collect(Collectors.toList());
        List<Content> objects = new CopyOnWriteArrayList<>();
        //多线程获取内容下载
        CompletableFuture.allOf(
                href.stream().map(x->{
                    return CompletableFuture.runAsync(()->{
                        try {
                            String s1 = HttpUtil.get(x.getUrl());
                            Document parse1 = Jsoup.parse(s1);
                            String text = parse1.getElementsByClass("txt_cont").select("h1").stream().findFirst().map(Element::text).orElse("第" + x.getIndex() + "章");
                            String content1 = parse1.getElementById("content1").textNodes().stream().map(c->CharSequenceUtil.trim(c.getWholeText(),-1)).filter(CharSequenceUtil::isNotBlank).collect(Collectors.joining("\n\r\n"));
                            log.info("内容为{}",text);
                            objects.add(new Content().setIndex(x.getIndex()).setData(text+"\r\n"+content1));
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },kanke).exceptionally(e->{
                        System.out.println(e);
                        return null;
                    });
                }).toArray(CompletableFuture[]::new)
        );
        //文件写入
        AtomicInteger integer = new AtomicInteger(1);
        while (true){
            objects.stream().filter(x-> Objects.equals(x.getIndex(), integer.get())).findFirst().ifPresent(x->{
                FileUtil.appendUtf8String(x.getData()+"\r\n","E:\\novels\\book\\盛唐日月1.txt");
                integer.getAndIncrement();
            });
        }

    }

    @Data
    @Accessors(chain = true)
    public static class Ca{
        private Integer index;
        private String name;
        private String url;
    }

    @Data
    @Accessors(chain = true)
    public static class Content{
        private Integer index;
        private String data;
    }
}
