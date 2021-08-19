package com.tiny.cloud.spider.common.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程池动态参数配置
 * @author administer
 */
@Data
@Slf4j
public class ThreadPoolConfig {
    private int corePoolSize = 500;
    private int maxPoolSize = 1000;
    private int queueSize = 1024;
    private int abortPolicyOption = 0;
}
