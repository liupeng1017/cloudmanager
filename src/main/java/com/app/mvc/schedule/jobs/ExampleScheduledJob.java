package com.app.mvc.schedule.jobs;

import com.app.mvc.schedule.AbstractScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jimin on 16/5/8.
 */
@Slf4j
public class ExampleScheduledJob extends AbstractScheduleJob {

    @Override
    public void schedule(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("任务Key: {}, 正在执行，执行时间: {}", jobName, dateFormat.format(Calendar.getInstance().getTime()));
        try {
            Thread.sleep(10000);
        } catch (Throwable t) {

        }
    }

    @Override
    public String scheduleId() {
        return "schedule.test";
    }

    @Override
    public String groupId() {
        return "group.test";
    }

}

