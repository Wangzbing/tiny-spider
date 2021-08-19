package com.tiny.cloud.bookspider.event.query;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description
 */
public class ContentEvent extends ApplicationEvent {
    public ContentEvent(Object source) {
        super(source);
    }

    public ContentEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
