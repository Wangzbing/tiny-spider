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
@Entity(name = "rank")
@EqualsAndHashCode(callSuper = true)
public class Rank extends BaseEntity {
  @Id
  private long relId;
  private long rankId;
  private long bookId;
  private long rankNumber;
}
