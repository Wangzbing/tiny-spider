package com.tiny.cloud.bookspider.model.repository;

import com.tiny.cloud.bookspider.model.entity.BookAddition;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangzb
 * @date 2021/8/16
 */
public interface BookAdditionRepository extends JpaRepository<BookAddition,Long> {
}
