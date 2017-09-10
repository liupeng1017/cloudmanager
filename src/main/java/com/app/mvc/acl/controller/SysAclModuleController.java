package com.app.mvc.acl.controller;

import com.app.mvc.acl.service.SysAclModuleService;
import com.app.mvc.acl.service.SysTreeService;
import com.app.mvc.acl.vo.AclModulePara;
import com.app.mvc.beans.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by jimin on 16/1/18.
 */
@Slf4j
@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping(value = "/page.do")
    public ModelAndView aclModulePage() {
        return new ModelAndView("aclModule");
    }

    @ResponseBody
    @RequestMapping(value = "/save.json")
    public JsonData saveAclModule(AclModulePara para) {
        sysAclModuleService.save(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/update.json")
    public JsonData updateAclModule(AclModulePara para) {
        sysAclModuleService.update(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/tree.json")
    public JsonData list() {
        return JsonData.success(sysTreeService.aclModuleTree());
    }

    @ResponseBody
    @RequestMapping(value = "/query.json")
    public JsonData getAclModule(@RequestParam("id") int id) {
        return JsonData.success(sysAclModuleService.findById(id));
    }

    @ResponseBody
    @RequestMapping(value = "/delete.json")
    public JsonData delete(@RequestParam("id") int id) {
        sysAclModuleService.deleteById(id);
        return JsonData.success();
    }
}
