package com.tiny.cloud.service.book.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tiny.cloud.spider.common.serializer.Long2StringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangzb
 * @date 2021/8/27
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentVO {
    private String content;
    @JsonSerialize(using = Long2StringSerializer.class)
    private Long preId;
    @JsonSerialize(using = Long2StringSerializer.class)
    private Long nextId;
}
