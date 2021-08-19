package com.tiny.cloud.spider.common.snowflake;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Petard
 */
@Slf4j
@Component
public class IDGenerator {
    /**
     * 终端ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long workerId = 0;
    /**
     * 数据中心ID
     */
    private static final long DATACENTER_ID = 1;

    private final Snowflake snowflake = IdUtil.createSnowflake(workerId, DATACENTER_ID);

    @PostConstruct
    public void init() {
        workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
    }

    public long nextId() {
        return snowflake.nextId();
    }
}
