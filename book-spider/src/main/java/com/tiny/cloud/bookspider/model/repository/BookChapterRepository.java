package com.tiny.cloud.bookspider.model.repository;

import com.tiny.cloud.bookspider.model.entity.BookChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author wangzb
 * @date 2021/8/16
 */
public interface BookChapterRepository extends JpaRepository<BookChapter,Long> {
    /**
     * 查询章节是否更新
     * @param s  BooKID
     * @param longValue ORDER
     * @return ENTITY
     */
    @Query("from book_chapter  a where  a.bookId=?1 and a.chapterOrder=?2")
    Optional<BookChapter> findByOption(Long s, long longValue);
}
