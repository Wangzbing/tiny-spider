package com.tiny.cloud.service.book.model.repository;

import com.tiny.cloud.service.book.model.entity.BookUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangzb
 * @date 2021/8/26
 * @description
 */
public interface BookUserRepository extends JpaRepository<BookUserEntity,Long> {
}
