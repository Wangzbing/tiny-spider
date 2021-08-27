package com.tiny.cloud.service.book.mapper;

import cn.hutool.core.lang.Pair;
import com.tiny.cloud.common.mybatis.MapperInterface;
import com.tiny.cloud.service.book.model.BookInfoDO;
import com.tiny.cloud.service.book.model.BookListVO;
import com.tiny.cloud.service.book.model.ChapterListBO;
import com.tiny.cloud.service.book.model.ContentVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

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

    @Select("SELECT a.chapter_id,a.chapter_name FROM book_chapter a WHERE a.chapter_id = #{id}")
    @Results({
            @Result(column = "chapter_id",property = "id"),
            @Result(column = "chapter_name",property = "name"),
    })
    ChapterListBO getChapterInfo(Long id);


    @Select("SELECT * FROM book_info a where a.book_Id=#{bookId}")
    BookInfoDO getBookInfo(Long bookId);


    @Select("SELECT a.content FROM book_chapter a WHERE a.chapter_id = #{id}")
    String getContentInfo(@Param("id") Long id);
    
    @Select("SELECT * FROM book_info a GROUP BY a.book_author_name order by LENGTH(a.book_author_name),RAND() DESC limit 36")
    List<BookInfoDO> qryBookList();


    List<BookListVO> getUserBooks(Long userId);

    @Select("SELECT a.book_id,a.chapter_order-1 as pre,a.chapter_order+1 as next FROM book_chapter a WHERE a.chapter_id = #{id}")
    @Results(id="pair",value={
        @Result(column = "{bookId=book_id,order=pre}",property = "preId",one = @One(select = "com.tiny.cloud.service.book.mapper.BookInfoMapper.getNext",fetchType = FetchType.EAGER)),
        @Result(column = "{bookId=book_id,order=next}",property = "nextId",one = @One(select = "com.tiny.cloud.service.book.mapper.BookInfoMapper.getNext",fetchType = FetchType.EAGER))
    })
    ContentVO getPreOrNext(Long id);

    @Select("SELECT a.chapter_id FROM book_chapter a WHERE a.book_id =#{bookId} AND a.chapter_order =#{order} limit 1")
    Long getNext(Long bookId,Long order);
}
