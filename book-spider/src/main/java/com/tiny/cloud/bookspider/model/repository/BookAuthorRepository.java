package com.tiny.cloud.bookspider.model.repository;

import com.tiny.cloud.bookspider.model.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangzb
 * @date 2021/8/16
 */
public interface BookAuthorRepository extends JpaRepository<BookAuthor,Long> {
    BookAuthor findByAuthorOriginId(Long authorId);

}
