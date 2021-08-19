package com.tiny.cloud.spider.common.strategy.enums;


import java.util.Map;

/**
 * @author wangzb
 * @date 2021/7/20
 * @description
 */
public enum SpiderStore {
    /*起点*/
    QI_DIAN(SpiderCategory.BOOK,"https://www.qidian.com"),
    /*微信读书*/
    WE_READ(SpiderCategory.BOOK,"https://weread.qq.com/web");

    private SpiderCategory category;
    private String baseUrl;
    private Map<String,String> params;

    SpiderStore() {
    }
    SpiderStore(SpiderCategory category,String baseUrl) {
        this.category=category;
        this.baseUrl=baseUrl;
    }

    public SpiderCategory getCategory() {
        return category;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
