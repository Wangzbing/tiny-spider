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
@Entity(name = "book_info")
@EqualsAndHashCode(callSuper = true)
public class BookInfo extends BaseEntity {
  @Id
  private Long bookId;
  private Long bookOriginId;
  private String bookName;
  private String bookRec;
  private Long bookAuthorId;
  private String bookAuthorName;
  private String bookIntro;
  private String bookInfoIntro;
  private String bookImage;
}
