package com.tiny.cloud.spider.common.strategy.annotations;

/**
 * @author amin
 */



import com.tiny.cloud.spider.common.strategy.enums.SpiderCategory;
import com.tiny.cloud.spider.common.strategy.enums.SpiderStore;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MessageHandler {
    /**
     * 每段
     *
     * @return String
     */
    SpiderCategory category();

    SpiderStore store();
}
