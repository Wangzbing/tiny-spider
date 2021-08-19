package com.tiny.cloud.bookspider.model.entity;

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
@Entity(name = "rel_info_category")
public class RelInfoCategory {
  @Id
  private long relId;
  private long bookId;
  private long cateId;
}
