package com.app.mvc.test;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jimin on 16/3/21.
 */
@Slf4j
public class TestThreadPool {

    private static ConcurrentMap<Integer, ThreadPoolExecutor> threadPoolExecutorMap = Maps.newConcurrentMap();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3000; i++) {
            threadPoolExecutorMap.put(i, newDefaultExecutor());
        }
        log.info("start!");
        for (int j = 1; j < 10; j++) {
            for (int p = 1; p < 10; p++) {
                try {
                    // 这里一定要使用execute, 而不要使用submit
                    // 原因是submit主要是为异步等待使用的, 返回FutureTask
                    // 如果使用submit,DiscardOldestPolicy中取出来的就是个FutureTask, 而不是普通的Runnable, 继续处理会阻塞进程
                    // 同时之前的进程如果已丢弃,这时会一直占着进程不释放
                    threadPoolExecutorMap.get(p).execute(new HttpWorker(p + "__" + j));
                } catch (Throwable e) {
                    log.error("exception", e);
                }
            }
        }
    }

    private static ThreadPoolExecutor newDefaultExecutor() {
        return new ThreadPoolExecutor(1,              // 核心池大小
                2,                                    // 最大线程数
                1200,                                 // 空闲等待时间
                TimeUnit.SECONDS,                     // 时间单位
                new ArrayBlockingQueue<Runnable>(40), // 循环数组 + 指定大小
                new DiscardOldestPolicy()             // 最早的丢弃, 可以根据需要调整
        );
    }

    /**
     * 自定义rejectedExecution逻辑
     */
    public static class DiscardOldestPolicy implements RejectedExecutionHandler {
        public DiscardOldestPolicy() {
        }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                HttpWorker httpWorker = (HttpWorker) e.getQueue().poll();
                e.execute(r);
                log.info("drop query: {}", httpWorker.getC());
            }
        }
    }

    /**
     * 自定义Runnable, 方面在rejectedExecution时处理逻辑
     */
    private static class HttpWorker implements Runnable {
        private String c;

        public HttpWorker(String c) {
            this.c = c;
        }

        public String getC() {
            return c;
        }

        @Override
        public void run() {
            long sum = 0;
            for (int q = 0; q < 100000000; q++) {
                sum += q;
            }
            log.info("c:{}, sum: {}", c, sum);
        }
    }
}
