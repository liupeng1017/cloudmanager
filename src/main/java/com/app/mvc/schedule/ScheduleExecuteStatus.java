package com.app.mvc.schedule;

import lombok.Getter;

/**
 * Created by jimin on 16/5/9.
 */
@Getter
public enum ScheduleExecuteStatus {
    RUNNING(0, "正在运行"),
    FINISHED(1, "已经完成"),
    EXCEPTION(2, "出现异常结束");

    private int code;
    private String desc;

    ScheduleExecuteStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
