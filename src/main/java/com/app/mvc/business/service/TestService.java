package com.app.mvc.business.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jimin on 16/3/25.
 */
@Slf4j
@Service
public class TestService {
    private ConcurrentMap<String, ThreadPoolExecutor> threadPoolExecutorMap = Maps.newConcurrentMap();

    public void start() {
        while (true) {
            getThreadPool("123").execute(new Runnable() {
                @Override
                public void run() {
                    log.info(System.currentTimeMillis() + "");
                }
            });
            try {
                Thread.currentThread().sleep(10000);
            } catch (Throwable t) {
                log.error("e", t);
            }
        }
    }

    public ThreadPoolExecutor getThreadPool(String key) {
        if (threadPoolExecutorMap.containsKey(key)) {
            return threadPoolExecutorMap.get(key);
        }
        log.info("init " + key);

        threadPoolExecutorMap.put(key,
                new ThreadPoolExecutor(2, 5, 120, TimeUnit.HOURS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.DiscardOldestPolicy()));
        return threadPoolExecutorMap.get(key);
    }
}
