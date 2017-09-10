package com.app.mvc.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by jimin on 16/5/8.
 */
@Getter
@Setter
@ToString
public class ScheduleJobSettingParam {

    @NotNull(message = "id不可以为空")
    private Integer id;

    private String cron;

    @NotNull(message = "状态不可以为空")
    private Integer status;
}
