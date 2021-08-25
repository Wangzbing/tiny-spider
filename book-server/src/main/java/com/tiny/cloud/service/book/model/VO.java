package com.tiny.cloud.service.book.model;

import lombok.Data;

import java.util.List;

/**
 * @author wangzb
 * @date 2021/8/24
 * @description
 */
public class VO {
    private String name;
    private List<BookInfoDO> my;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookInfoDO> getMy() {
        return my;
    }

    public void setMy(List<BookInfoDO> my) {
        this.my = my;
    }
}
