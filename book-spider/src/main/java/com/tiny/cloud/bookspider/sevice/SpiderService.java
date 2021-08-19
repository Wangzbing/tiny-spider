package com.tiny.cloud.bookspider.sevice;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
public interface SpiderService {
    /**
     * 保存详情
     */
    void saveInfo();

    /**
     * 保存排行
     */
    void saveRank();

    void saveContent();
}
