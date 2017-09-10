package com.app.mvc.acl.service;

import com.app.mvc.acl.enums.CacheKeyConstants;
import com.app.mvc.beans.JsonMapper;
import com.app.mvc.redis.RedisPool;
import com.google.common.base.Joiner;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * Created by jimin on 16/2/4.
 */
@Slf4j
@Service
public class SysCacheService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;

    /**
     * 添加缓存数据
     *
     * @param toSavedValue   要保存的数据,必须序列化
     * @param timeoutSeconds 缓存失效时间,单位:秒
     * @param prefix         缓存key前缀
     */
    public void saveCache(String toSavedValue, int timeoutSeconds, @NonNull CacheKeyConstants prefix) {
        saveCache(toSavedValue, timeoutSeconds, prefix, null);
    }

    /**
     * 添加缓存数据
     * <p>
     * 这里要求必填一个缓存的时间，如果个别场景明确可以一直缓存及在代码里自己的更新机制，可以封装一个不带过期时间的方法
     *
     * @param toSavedValue   要保存的数据,必须序列化
     * @param timeoutSeconds 缓存失效时间,单位:秒
     * @param prefix         缓存key前缀
     * @param keys           缓存key的每一项
     */
    public void saveCache(String toSavedValue, int timeoutSeconds, @NonNull CacheKeyConstants prefix, String... keys) {
        if (toSavedValue == null) { // 空值不被保存,返回为空时代表出现异常情况
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Throwable t) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys), t);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 失效缓存数据
     *
     * @param prefix 缓存key前缀
     * @param keys   缓存key的每一项
     */
    public void delCache(@NonNull CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.del(cacheKey);
        } catch (Throwable t) {
            log.error("expire cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys), t);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 从cache获取指定key的数据
     * 注意:为空时代表获取出现异常
     *
     * @param prefix 缓存key的前缀
     * @param keys   缓存key的每一项
     * @return 缓存中数据
     */
    public String getFromCache(@NonNull CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            String value = shardedJedis.get(cacheKey);
            return value;
        } catch (Throwable t) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys), t);
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 生成cache key
     *
     * @param prefix 前缀
     * @param keys   组成key的列表
     * @return prefix_K1_K2..._Kn:
     */
    private String generateKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
