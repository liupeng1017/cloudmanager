package com.app.mvc.test;

import java.util.TimeZone;

/**
 * Created by jimin on 16/8/14.
 */
public class TestTimezone {

    public static void main(String[] args) throws Exception {
        String[] s = TimeZone.getAvailableIDs();
        for(String timezoneId : s) {
            TimeZone timeZone = TimeZone.getTimeZone(timezoneId);
            System.out.println(timezoneId + "\t" + timeZone.getDisplayName());
        }
    }
}
