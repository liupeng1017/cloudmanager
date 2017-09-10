package com.app.mvc.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by jimin on 16/5/9.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleExecuteResult {

    private int id;

    private String scheduleId;

    private Date startTime;

    private Date endTime;

    private int status;
}
