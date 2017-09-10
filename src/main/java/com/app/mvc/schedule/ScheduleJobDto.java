package com.app.mvc.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jimin on 16/5/14.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleJobDto {

    private int id;

    private String groupId;

    private String scheduleId;

    private String cron;

    private int status;

    private String lastExecuteTime;

    private String nextExecuteTime;

    private long costMillSeconds;
}
