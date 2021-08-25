package com.tiny.cloud.service.book.model;

import lombok.Data;

/**
 * @author wangzb
 * @date 2021/8/20
 * @description
 */
@Data
public class BookInfoDO {
    private String bookName;
    private Long bookId;
    private String bookAuthorName;
    private Long authorId;
    private String bookIntro;
    private String bookImage;
}
