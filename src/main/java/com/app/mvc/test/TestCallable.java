package com.app.mvc.test;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jimin on 16/3/29.
 */
public class TestCallable {

    private static final ExecutorService threadPool = new ThreadPoolExecutor(2, 5, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));

    private static int flag = 0;

    // test future.get() 的NPE
    public static void main(String[] args) throws Exception {
        Future<Long> future = threadPool.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                //                return 1l;
                List<Long> list = Lists.newArrayList();
                if (flag >= 0) {
                    list = null;
                }
                return list.get(0);
            }
        });
        System.out.println(future.get());
    }
}
