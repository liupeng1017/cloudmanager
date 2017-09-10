package com.app.mvc.common;

import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import com.app.mvc.util.DateTimeUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by jimin on 16/5/6.
 */
@Slf4j
public class QPSLimiter {

    private final static String SWITCH_OPEN = "on";
    private final static String SWITCH_CLOSED = "off";

    private static final Cache<String, Integer> urlQpsCache = CacheBuilder.newBuilder() //
            .expireAfterAccess(1, TimeUnit.MINUTES)  // 每1分钟更新一次
            .build();

    /**
     * 全局的限流开关是否开启
     *
     * @return true:开启,false:不开启
     */
    public static boolean isGlobalLimitSwitchOpen() {
        return GlobalConfig.getStringValue(GlobalConfigKey.GLOBAL_QPS_LIMIT_SWITCH, SWITCH_CLOSED).equals(SWITCH_OPEN);
    }

    /**
     * 尝试请求,如果qps被限制,则等待执行
     *
     * @param permits
     * @param key
     */
    public static void acquire(int permits, String key) {
        while (canAcquire(permits, key)) {
            ThreadHelper.safeSleep(1000);
        }
    }

    /**
     * 是否限制qps
     *
     * @param permits 许可的个数, 基本都为1
     * @param key     生成config配置的key, 可以为url等
     * @return true:限制,false:不限制
     */
    private static boolean canAcquire(int permits, String key) {
        String configKey = GlobalConfigKey.QPS_LIMIT_PREFFIX + key + GlobalConfigKey.QPS_LIMIT_SUFFIX;
        if (!isGlobalLimitSwitchOpen()) {
            return false;
        }
        if (StringUtils.isEmpty(configKey)) {
            return false;
        }
        // 先检查对应的key的是否有配置
        if (StringUtils.isBlank(GlobalConfig.getStringValue(configKey, ""))) {
            return false;
        }
        String time = DateTimeUtil.timeFrom(new Date());
        String cacheKey = configKey + "." + time;

        int result = acquire(permits, configKey, cacheKey);
        // 如果拿到许可, 则为不限制
        return !(result == permits);
    }

    /**
     * 获得许可
     *
     * @param permits 许可的个数, 基本都为1
     * @return 拿到许可的个数
     */
    private static synchronized int acquire(int permits, String qconfigKey, String cacheKey) {
        Integer count = urlQpsCache.getIfPresent(cacheKey);
        if (count == null) {
            // 缓存中没有,需要按照配置往cache中添加新的key和value
            count = createCache(qconfigKey, cacheKey);
        }
        if (count >= permits) {
            decreaseCacheQps(cacheKey, permits);
            return permits;
        }
        return 0;
    }

    /**
     * 根据qconfig中的值创建cache
     */
    private static int createCache(String qconfigKey, String cacheKey) {
        int qconfigQps = GlobalConfig.getIntValue(qconfigKey, 0);
        if (log.isDebugEnabled()) {
            log.debug("create cache, key:{}, value:{}", cacheKey, qconfigQps);
        }
        urlQpsCache.put(cacheKey, qconfigQps);
        return qconfigQps;
    }

    /**
     * 从cache缓存的数据中减少拿到的许可数
     *
     * @param permits 许可的个数, 基本都为1
     */
    private static void decreaseCacheQps(String cacheKey, int permits) {
        Integer cacheQps = urlQpsCache.getIfPresent(cacheKey);
        if (log.isDebugEnabled()) {
            log.debug("update cache, key:{}, value:{}", cacheKey, cacheQps - permits);
        }
        if (cacheQps == null) {
            return;
        }
        urlQpsCache.put(cacheKey, cacheQps - permits);
    }

}
