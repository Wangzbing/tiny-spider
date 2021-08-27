package com.tiny.cloud.bookspider.model.repository;

import com.tiny.cloud.bookspider.model.entity.RelInfoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description 内容详情关联
 */
public interface RelInfoCategoryRepository extends JpaRepository<RelInfoCategory,Long> {
    RelInfoCategory findByBookIdAndCateId(long book, Long aLong);

}
