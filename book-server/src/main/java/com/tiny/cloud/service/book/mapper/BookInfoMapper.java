package com.tiny.cloud.service.book.mapper;

import com.tiny.cloud.common.mybatis.MapperInterface;
import com.tiny.cloud.service.book.model.BookInfoDO;
import com.tiny.cloud.service.book.model.ChapterListBO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangzb
 * @date 2021/8/20
 * @description
 */
@Mapper
public interface BookInfoMapper extends MapperInterface {
    @Select("SELECT a.chapter_id,a.chapter_name FROM book_chapter a WHERE a.book_id = #{id} ORDER BY a.chapter_order")
    @Results({
            @Result(column = "chapter_id",property = "id"),
            @Result(column = "chapter_name",property = "name"),
    })
    List<ChapterListBO> qryChapterList(Long id);

    @Select("SELECT a.content FROM book_chapter a WHERE a.chapter_id = #{id}")
    String getContentInfo(@Param("id") Long id);
    
    @Select("SELECT * FROM book_info a GROUP BY a.book_author_name order by LENGTH(a.book_author_name),RAND() DESC limit 36")
    List<BookInfoDO> qryBookList();
}
