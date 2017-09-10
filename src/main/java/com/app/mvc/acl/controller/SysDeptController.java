package com.app.mvc.acl.controller;

import com.app.mvc.acl.service.SysDeptService;
import com.app.mvc.acl.service.SysTreeService;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.acl.vo.DeptPara;
import com.app.mvc.beans.JsonData;
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
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping(value = "/page.do")
    public ModelAndView deptPage() {
        return new ModelAndView("dept");
    }

    @ResponseBody
    @RequestMapping(value = "/save.json")
    public JsonData saveDept(DeptPara para) {
        para.setSupplierId(RequestHolder.getCurrentUser().getSupplierId());
        sysDeptService.save(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/update.json")
    public JsonData updateDept(DeptPara para) {
        sysDeptService.update(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/tree.json")
    public JsonData list() {
        return JsonData.success(sysTreeService.deptTree());
    }

    @ResponseBody
    @RequestMapping(value = "/query.json")
    public JsonData getDept(@RequestParam("id") int id) {
        return JsonData.success(sysDeptService.findById(id));
    }

    @ResponseBody
    @RequestMapping(value = "/delete.json")
    public JsonData delete(@RequestParam("id") int id) {
        sysDeptService.deleteById(id);
        return JsonData.success();
    }

}
