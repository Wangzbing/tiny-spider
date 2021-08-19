package com.tiny.cloud.bookspider.model.entity;


import com.tiny.cloud.common.config.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author administer
 */
@Data
@Table
@Entity(name = "book_chapter")
@EqualsAndHashCode(callSuper = true)
public class BookChapter extends BaseEntity {
  @Id
  private Long chapterId;
  private Long bookId;
  private String chapterName;
  private Long contentLength;
  private String chapterUrl;
  private String content;
  private Long order;
}
