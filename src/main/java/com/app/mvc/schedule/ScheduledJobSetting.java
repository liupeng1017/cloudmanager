package com.app.mvc.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jimin on 16/5/8.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledJobSetting {

    private int id;

    private String scheduleId;

    private String groupId;

    private String cron;

    private String classPath;

    private int status = ScheduleJobStatus.INIT.getCode();
}
