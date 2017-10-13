package com.app.mvc.acl.controller;

import com.app.mvc.acl.domain.SysLog;
import com.app.mvc.acl.service.SysCoreService;
import com.app.mvc.acl.service.SysLogService;
import com.app.mvc.acl.vo.LogPara;
import com.app.mvc.beans.JsonData;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.beans.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by jimin on 16/1/23.
 */
@Slf4j
@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;
    @Resource
    private SysCoreService sysCoreService;

    @RequestMapping(value = "/page.do")
    public ModelAndView logPage() {
        return new ModelAndView("log");
    }

    @ResponseBody
    @RequestMapping(value = "/fuzzySearch.json")
    public JsonData fuzzySearch(LogPara para, PageQuery page) {
        PageResult<SysLog> list = sysLogService.getPageByFuzzySearch(para, page);
        return JsonData.success(list);
    }

    @ResponseBody
    @RequestMapping(value = "/list.json")
    public JsonData list(LogPara para, PageQuery page) {
        PageResult<SysLog> list = sysLogService.getPageByFuzzySearch(para, page);
        return JsonData.success(list);
    }
    @ResponseBody
    @RequestMapping(value = "/query.json")
    public JsonData getDept(@RequestParam("id") int id) {
        return JsonData.success(sysLogService.findById(id));
    }

    @ResponseBody
    @RequestMapping(value = "/recover.json")
    public JsonData recover(@RequestParam("id") int id) {
        sysCoreService.recover(id);
        return JsonData.success();
    }
}
