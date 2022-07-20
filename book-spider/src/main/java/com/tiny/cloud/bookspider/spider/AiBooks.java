package com.tiny.cloud.bookspider.spider;

import cn.hutool.http.*;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhongbing.wang@xiaoxitech.com
 * @date 2022/7/19
 */
@Slf4j
public class AiBooks {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("huoduan_verifycode","080129");
        String post = HttpUtil.post("https://www.aibooks.cc/books/9538.html", stringStringHashMap);
        Document parse = Jsoup.parse(post);
        AtomicReference<String> name = new AtomicReference<>(null);
        parse.getElementsByClass("article-title").stream().findFirst().ifPresent(x->{
            String text = x.text();
            name.set(text);
        });
        Elements downloadUrl = parse.getElementsByClass("dltable");
        AtomicReference<String> url = new AtomicReference<>(null);
        downloadUrl.select("a").stream().filter(cc->cc.text().contains("蓝奏")).findFirst().ifPresent(x->{
            String href = x.attr("href");
            url.set(href);
        });
        Elements elementsByClass = parse.getElementsByClass("alert alert-success");
        AtomicReference<String> code = new AtomicReference<>(null);
        elementsByClass.stream().filter(cc->cc.text().contains("蓝奏")).findFirst().ifPresent(x->{
            String text = x.text();
            String[] split = text.split(" ");
            Arrays.stream(split).filter(pw->pw.contains("蓝奏")).findFirst().ifPresent(g->{
                String[] split1 = g.split("：");
                code.set(split1[1]);
            });
        });
        //查询下载页
        if (code.get()==null||url.get()==null){
            return;
        }
        System.out.println(code.get());
        System.out.println(url.get());
        String s = url.get();
        String s1 = HttpUtil.get(s, 1000);
        Document parse1 = Jsoup.parse(s1);
        Elements script = parse1.select("script");

        script.stream().filter(x->x.html().contains("/filemoreajax.php")).findFirst().ifPresent(x->{
            //lx=2&fid=5095299&uid=228627&pg=1&rep=0&t=1658215896&k=74ad0742b2ffd27f3e8b4024b22dd511&up=1&ls=1&pwd=bisc

            String html = x.html();
            String[] split = html.split("\n\t");
            Map<String, Object> param = new HashMap<>();
            Arrays.stream(split).forEach(cc->{
                if (cc.contains("'lx'")){
                    String s2 = cc.replace(",", "").split(":")[1];
                    param.putIfAbsent("lx",s2);
                }
                if (cc.contains("'fid'")){
                    String s2 = cc.replace(",", "").split(":")[1];
                    param.putIfAbsent("fid",s2);
                }
                if (cc.contains("'uid'")){
                    String s2 = cc.replace(",", "").replace("'","").split(":")[1];
                    param.putIfAbsent("lx",s2);
                }
                if (cc.contains("'rep'")){
                    String s2 = cc.replace(",", "").replace("'","").split(":")[1];
                    param.putIfAbsent("rep",s2);
                }
                if (cc.contains("'up'")){
                    String s2 = cc.replace(",", "").split(":")[1];
                    param.putIfAbsent("up",s2);
                }
                if (cc.contains("'ls'")){
                    String s2 = cc.replace(",", "").split(":")[1];
                    param.putIfAbsent("ls",s2);
                }
                if (cc.contains("pgs =")){
                    String s2 = cc.replace(";", "").split("=")[1];
                    param.putIfAbsent("pg",s2);
                }
                if (cc.contains("'t'")){
                    //获取随机值
                    String s2 = cc.replace(",", "").replace("'","").split(":")[1];
                    Arrays.stream(split).filter(xx->xx.contains("var "+s2)).findFirst().ifPresent(cx->{
                        String s3 = cx.replace(";", "").replace("'", "").split("=")[1];
                        param.putIfAbsent("t",s3.trim());
                    });
                }
                if (cc.contains("'k'")){
                    String s2 = cc.replace(",", "").replace("'","").split(":")[1];
                    Arrays.stream(split).filter(xx->xx.contains("var "+s2)).findFirst().ifPresent(cx->{
                        String s3 = cx.replace(";", "").replace("'", "").split("=")[1];
                        param.putIfAbsent("k",s3.trim());
                    });
                }
            });
            param.put("pwd",code.get());
            String bash ="https://aibooks.lanzouh.com";
            String post1 = HttpUtil.post(bash + "/filemoreajax.php", param);
            JSONObject jsonObject = JSONUtil.parseObj(post1);
            JSONArray text = jsonObject.getJSONArray("text");
            for (int i = 0; i < text.size(); i++) {
                JSONObject jsonObject1 = text.getJSONObject(i);
                String id = jsonObject1.getStr("id");
                String bookName = jsonObject1.getStr("name_all");
                String s2 = HttpUtil.get(bash +"/"+ id);
                Document parse2 = Jsoup.parse(s2);
                Elements iframe = parse2.select("iframe");
                iframe.stream().findFirst().ifPresent(cc->{
                    String src = cc.attr("src");
                    String s3 = HttpUtil.get(bash + src);
                    Document parse3 = Jsoup.parse(s3);
                    parse3.select("script").stream().filter(xa->xa.html().contains("vsign")).findFirst().ifPresent(qa->{
                        String html1 = qa.html();
                        String[] split1 = html1.split("\n\t\t");
                        Map<String, String> newParam = new HashMap<>();
                        Map<String, Object> posyParam = new HashMap<>();
                        Arrays.stream(split1).forEach(ab->{
                            if (ab.contains("var ajaxdata")){
                                String ajaxdata = ab.replace(";", "").replace("'","").split("=")[1].trim();
                                newParam.putIfAbsent("signs",ajaxdata);
                            }
                            if (ab.contains("var cwebsignkeyc")){
                                String key = ab.replace(";", "").replace("'","").split("=")[1].trim();
                                newParam.putIfAbsent("websignkey",key);
                            }
                            if (ab.contains("var awebsigna")){
                                String data = ab.replace(";", "").replace("'","").split("=")[1].trim();
                                newParam.putIfAbsent("websign",data);
                            }
                            if (ab.contains("var vsign")){
                                String data = ab.replace(";", "").replace("'","").split("=")[1].trim();
                                newParam.putIfAbsent("sign",data);
                            }
                            if (ab.trim().startsWith("data :")){
                                newParam.putIfAbsent("action","downprocess");
                                newParam.putIfAbsent("ves","1");
                            }
                        });
                        //referer: https://aibooks.lanzouh.com/fn?A2UHbVkyVzUAZVc1VjJRZQNrATwAeVEnVW9QZwJoWm5VbVI3CGwDYwZlBWRUMQAnBClQMFNuAnMHaQBhXW8HbQNmBylZOVdyADBXWVZv
                        HttpResponse referer = HttpUtil.createRequest(Method.POST, bash + "/ajaxm.php").formStr(newParam).header("referer", bash + src).executeAsync();
                        String body = referer.body();
                        JSONObject dataObject = JSONUtil.parseObj(body);
//                        date.dom+"/file/"+ date.url
                        String dom = dataObject.getStr("dom");
                        String url1 = dataObject.getStr("url");
                        String s4 = name.get();
                        System.out.println(dom + "/file/" + url1);
                        getRedirectUrl1(dom +"/file/" + url1,s4,bookName);
                    });
                });
            }

        });
    }

    public static void getRedirectUrl1(String url,String desc,String bookName)  {
        RequestConfig config = RequestConfig.custom().setRedirectsEnabled(false).build();//不允许重定向
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
        HttpClientContext context = HttpClientContext.create();
        BasicClientCookie down_ip = new BasicClientCookie("down_ip", "1");
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        basicCookieStore.addCookie(down_ip);
        context.setCookieStore(basicCookieStore);
        url = url.replace("https", "http");
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept-Language","zh-CN,zh;q=0.8");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpget, context);
            org.apache.http.Header locations = response.getFirstHeader("Location");
            System.out.println(locations);
            System.out.println("開始下載");
            System.out.println("E:\\novels\\book\\" + desc + "\\" + bookName);
            HttpUtil.downloadFile(locations.getValue(),"E:\\novels\\book\\"+desc+"\\"+bookName);
            System.out.println("下載結束");
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
