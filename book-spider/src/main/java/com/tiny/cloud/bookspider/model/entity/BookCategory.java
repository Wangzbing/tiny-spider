package com.tiny.cloud.bookspider.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author administer
 */
@Data
@Table
@Entity(name = "book_category")
public class BookCategory{
  @Id
  private Long cateId;
  private String cateName;
  private String cateUrl;
  private Long parentCateId;
}
