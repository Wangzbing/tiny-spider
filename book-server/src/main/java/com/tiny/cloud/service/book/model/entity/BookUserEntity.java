package com.tiny.cloud.service.book.model.entity;

import com.tiny.cloud.spider.common.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author wangzb
 * @date 2021/8/26
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity(name = "book_user")
public class BookUserEntity extends BaseEntity {
    @Id
    private Long id;
    private Long bookId;
    private Long userId;
    /**
     * 是否收藏
     */
    private Boolean isCollect;
}
