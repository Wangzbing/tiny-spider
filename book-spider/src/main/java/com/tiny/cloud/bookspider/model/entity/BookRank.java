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
@Entity(name = "book_rank")
@EqualsAndHashCode(callSuper = true)
public class BookRank  extends BaseEntity {
  @Id
  private long rankId;
  private String rankName;
  private String rankCode;
  private String rankStyle;
  private String rankCate;
  private long rankHour;
}
