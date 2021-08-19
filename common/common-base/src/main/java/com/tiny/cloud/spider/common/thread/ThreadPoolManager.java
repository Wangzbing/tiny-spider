package com.tiny.cloud.spider.common.thread;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池管理器
 * 获取线程池
 * @author administer
 */
@Component
@Data
@Slf4j
public class ThreadPoolManager {

    private Map<String, ThreadPoolExecutor> mThreadPoolExecutorHashMap = new HashMap<>();

    private ThreadPoolConfig threadPoolConfig= new ThreadPoolConfig();

    public ThreadPoolExecutor getDefaultThreadPoolExecutor() {
        return createThreadPool("DEFAULT");
    }

    public Map<String, ThreadPoolExecutor> getThreadPoolExecutorHashMap() {
        return mThreadPoolExecutorHashMap;
    }

    public ThreadPoolExecutor createThreadPool(String name) {
        if (mThreadPoolExecutorHashMap.get(name) != null) {
            return mThreadPoolExecutorHashMap.get(name);
        }
        RejectedExecutionHandler abortPolicy = getRejectedExecutionHandler();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(), threadPoolConfig.getMaxPoolSize(),
                1L, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(threadPoolConfig.getQueueSize()), new ThreadFactoryBuilder().build(), abortPolicy);
        mThreadPoolExecutorHashMap.put(name, executor);
        return executor;
    }

    private RejectedExecutionHandler getRejectedExecutionHandler() {
        RejectedExecutionHandler abortPolicy;
        switch (threadPoolConfig.getAbortPolicyOption()) {
            case 1:
                abortPolicy = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case 2:
                abortPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            case 3:
                abortPolicy = new ThreadPoolExecutor.AbortPolicy();
                break;
            default:
                abortPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        }
        return abortPolicy;
    }
}
