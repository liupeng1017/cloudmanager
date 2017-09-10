package com.app.mvc.schedule;

import com.app.mvc.beans.PageQuery;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/5/8.
 */
@DBRepository
public interface ScheduleExecuteResultDao {

    void save(ScheduleExecuteResult setting);

    void update(ScheduleExecuteResult setting);

    List<ScheduleExecuteResult> getPageByScheduleId(@Param("scheduleId") String scheduleId, @Param("page") PageQuery page);

    int countByScheduleId(@Param("scheduleId") String scheduleId);

    ScheduleExecuteResult findLastExecute(@Param("scheduleId") String scheduleId);
}
