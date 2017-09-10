package com.app.mvc.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jimin on 16/5/9.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleExecuteResultDto {

    private String scheduleId;

    private String start;

    private String end;

    private int status;

    private long costMillSeconds;
}
