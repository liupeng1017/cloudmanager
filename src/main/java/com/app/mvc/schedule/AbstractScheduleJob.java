package com.app.mvc.schedule;

import com.app.mvc.common.SpringHelper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by jimin on 16/5/8.
 */
@Slf4j
public abstract class AbstractScheduleJob implements Job {

    private static ScheduleExecuteResultDao scheduleExecuteResultDao = SpringHelper.popBean(ScheduleExecuteResultDao.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String scheduleId = groupId() + "_" + scheduleId();
        log.info("定时任务[{}]开始执行", scheduleId);
        ScheduleExecuteResult scheduleExecuteResult = ScheduleExecuteResult.builder().scheduleId(scheduleId).status(ScheduleExecuteStatus.RUNNING.getCode())
                .build();
        scheduleExecuteResultDao.save(scheduleExecuteResult);
        try {
            schedule(context);
            scheduleExecuteResult.setStatus(ScheduleExecuteStatus.FINISHED.getCode());
        } catch (Throwable t) {
            scheduleExecuteResult.setStatus(ScheduleExecuteStatus.EXCEPTION.getCode());
            log.error("定时任务[" + scheduleId + "]执行出现异常", t);
        }
        scheduleExecuteResultDao.update(scheduleExecuteResult);
        log.info("定时任务[{}]执行结束", scheduleId);
    }

    public abstract void schedule(JobExecutionContext context) throws JobExecutionException;

    public abstract String scheduleId();

    public abstract String groupId();
}
