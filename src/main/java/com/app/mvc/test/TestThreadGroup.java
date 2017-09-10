package com.app.mvc.test;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jimin on 16/3/20.
 */
@Slf4j
public class TestThreadGroup {
    public static void main(String[] args) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数加倍
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slackList = new Thread[estimatedSize];
        // 获取根线程组的所有线程
        int actualSize = topGroup.enumerate(slackList);
        // copy into a list that is the exact size
        Thread[] list = new Thread[actualSize];
        System.arraycopy(slackList, 0, list, 0, actualSize);
        log.info("Thread list size == " + list.length);
        for (Thread thread : list) {
            log.info(thread.getName());
        }

    }
}
