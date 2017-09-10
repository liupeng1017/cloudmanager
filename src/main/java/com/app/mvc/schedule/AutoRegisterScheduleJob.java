package com.app.mvc.schedule;

import com.app.mvc.common.SpringHelper;
import com.app.mvc.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jimin on 16/5/9.
 */
@Slf4j
public class AutoRegisterScheduleJob {

    public static void registerWhenStartUp() {
        log.info("auto register schedule job start");
        ScheduleJobSettingDao scheduleJobSettingDao = SpringHelper.popBean(ScheduleJobSettingDao.class);
        List<Class> clazzList = ClassUtil.getSubClassList(AbstractScheduleJob.class, "com.app.mvc");
        if (CollectionUtils.isEmpty(clazzList)) {
            return;
        }
        for (Class clazz : clazzList) {
            String className = clazz.getName();
            if (scheduleJobSettingDao.countByClassPath(className) == 0) {
                try {
                    Object instance = clazz.newInstance(); // 此处需要使用实例化后的对象去invoke才能拿到方法的返回值
                    Method scheduleIdMethod = clazz.getDeclaredMethod("scheduleId");
                    Method groupIdMethod = clazz.getDeclaredMethod("groupId");
                    log.info("find a new schedule job to register, {}", className);
                    ScheduledJobSetting setting = ScheduledJobSetting.builder().groupId((String) groupIdMethod.invoke(instance))
                            .scheduleId((String) scheduleIdMethod.invoke(instance)).classPath(className).build();
                    scheduleJobSettingDao.save(setting);
                } catch (Throwable t) {
                    log.error("auto register schedule job exception, name: " + className, t);
                }
            }
        }
        log.info("auto register schedule job end");
    }
}
