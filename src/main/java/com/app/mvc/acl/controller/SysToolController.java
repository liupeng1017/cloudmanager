package com.app.mvc.acl.controller;

import com.app.mvc.acl.service.SysToolService;
import com.app.mvc.beans.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by jimin on 16/2/2.
 */
@Slf4j
@Controller
@RequestMapping("/sys/tool")
public class SysToolController {

    @Resource
    private SysToolService sysToolService;

    @ResponseBody
    @RequestMapping(value = "/regexp.json")
    public JsonData regexp(@RequestParam("url") String url, @RequestParam("rule") String rule) {
        return JsonData.success(sysToolService.checkRegexp(url, rule));
    }

    @ResponseBody
    @RequestMapping(value = "/select.json")
    public JsonData select(@RequestParam("sql") String sql) {
        return JsonData.success(sysToolService.executeSelect(sql));
    }

    @ResponseBody
    @RequestMapping(value = "/update.json")
    public JsonData update(@RequestParam("sql") String sql) {
        sysToolService.executeUpdate(sql);
        return JsonData.success();
    }
}
