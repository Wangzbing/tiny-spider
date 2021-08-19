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
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity(name = "book_addition")
public class BookAddition extends BaseEntity {
  @Id
  private Long additionId;
  private Long bookId;
  private Integer allContentLength;
  private Integer allRecommends;
  private Integer webRecommends;
}
