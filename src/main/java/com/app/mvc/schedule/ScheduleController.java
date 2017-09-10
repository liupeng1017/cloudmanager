package com.app.mvc.schedule;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.beans.JsonData;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.util.DateTimeUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by jimin on 16/5/8.
 */
@Controller
@RequestMapping("/admin/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @ResponseBody
    @RequestMapping("/page.do")
    public ModelAndView page() {
        return new ModelAndView("schedule");
    }

    @ResponseBody
    @RequestMapping("/executePage.do")
    public ModelAndView executePage(@RequestParam("scheduleId") String scheduleId) {
        ModelAndView modelAndView = new ModelAndView("schedule_execute");
        modelAndView.addObject("scheduleId", scheduleId);
        return modelAndView;
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(ScheduleJobSettingParam param) throws Exception {
        BaseConvert.checkPara(param);
        ScheduledJobSetting setting = scheduleService.findJobSetting(param.getId());
        if (setting == null) {
            return JsonData.error("not found setting");
        }
        setting.setCron(param.getCron());
        setting.setStatus(param.getStatus());
        scheduleService.updateJobSetting(setting);
        return JsonData.success();
    }

    @RequestMapping("/all.json")
    @ResponseBody
    public JsonData getAll() {
        return JsonData.success(scheduleService.getAll());
    }

    @RequestMapping("/getListByGroupId.json")
    @ResponseBody
    public JsonData getListByGroupId(@RequestParam("groupId") String groupId) {
        return JsonData.success(scheduleService.getListByGroupId(groupId));
    }

    @RequestMapping("/cron.json")
    @ResponseBody
    public JsonData cronTest(@RequestParam("cron") String cron, @RequestParam(value = "fireTimes", defaultValue = "5") int fireTimes) {
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        try {
            cronTriggerImpl.setCronExpression(cron);
        } catch (ParseException e) {
            return JsonData.error("cron parse error");
        }
        final List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, fireTimes);
        return JsonData.success(Lists.transform(dates, new Function<Date, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Date input) {
                return DateTimeUtil.dateTimeFrom(input);
            }
        }));
    }

    @RequestMapping("/results.json")
    @ResponseBody
    public JsonData resultSearch(@RequestParam("scheduleId") String scheduleId, PageQuery page) {
        return JsonData.success(scheduleService.getPageByScheduleId(scheduleId, page));
    }
}
