package com.tiny.spider.common.web;

import java.lang.annotation.*;

/**
 * @author Amin
 */
@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RestfulBody {
}
