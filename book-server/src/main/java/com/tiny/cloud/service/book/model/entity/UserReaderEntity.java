package com.tiny.cloud.service.book.model.entity;

import com.tiny.cloud.spider.common.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wangzb
 * @date 2021/8/26
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity(name = "user_reader")
public class UserReaderEntity extends BaseEntity {
    @Id
    private Long id;
    private Long bookUserId;
    private Integer readChapter;
}
