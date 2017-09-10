package com.app.mvc.common;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jimin on 16/6/22.
 */
@Slf4j
public class ThreadHelper {

    public static void safeSleep(long millseconds) {
        try {
            Thread.currentThread().sleep(millseconds);
        } catch (Throwable t) {
            log.error("thread sleep exception", t);
        }
    }
}
