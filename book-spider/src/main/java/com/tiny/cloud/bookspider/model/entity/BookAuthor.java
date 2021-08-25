package com.tiny.cloud.bookspider.model.entity;

import com.tiny.cloud.spider.common.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author administer
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity(name = "book_author")
public class BookAuthor extends BaseEntity {
  @Id
  private Long authorId;
  private Long authorOriginId;
  private String authorName;
  private String authorCover;
  private Integer authorLevel;
  private String authorContentSum;
  private Long authorDay;
  private String authorIntro;
}
