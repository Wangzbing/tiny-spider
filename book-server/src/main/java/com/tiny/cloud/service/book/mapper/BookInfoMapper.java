package com.tiny.cloud.service.book.mapper;

import com.tiny.cloud.common.mybatis.MapperInterface;
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
    @Select("SELECT a.chapter_id,a.chapter_name FROM book_chapter a WHERE a.book_id = 1427576618427219968 ORDER BY a.chapter_order")
    @Results({
            @Result(column = "chapter_id",property = "id"),
            @Result(column = "chapter_name",property = "name"),
    })
    List<ChapterListBO> qryChapterList();

    @Select("SELECT a.content FROM book_chapter a WHERE a.chapter_id = #{id}")
    String getContentInfo(@Param("id") Long id);
}
