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
@Entity(name = "spider")
@EqualsAndHashCode(callSuper = true)
public class Spider extends BaseEntity {
  @Id
  private Long id;
  private String name;
  private String store;
  private String url;
  private Integer status;
  private Boolean success;
}
