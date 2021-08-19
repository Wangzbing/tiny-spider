package com.tiny.cloud.bookspider.model.repository;

import com.tiny.cloud.bookspider.model.entity.Spider;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description 爬虫进度
 */
public interface SpiderRepository extends JpaRepository<Spider,Long> {
}
