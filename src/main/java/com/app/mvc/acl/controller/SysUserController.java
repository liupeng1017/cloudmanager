package com.app.mvc.acl.controller;

import com.app.mvc.acl.service.SysTreeService;
import com.app.mvc.acl.service.SysUserService;
import com.app.mvc.acl.vo.UserPara;
import com.app.mvc.beans.JsonData;
import com.app.mvc.beans.PageQuery;
import com.google.common.base.Preconditions;
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
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping(value = "/page.do")
    public ModelAndView userPage() {
        return new ModelAndView("user");
    }

    @RequestMapping(value = "/noAuth.do")
    public ModelAndView noAuthPage() {
        return new ModelAndView("noAuth");
    }

    @ResponseBody
    @RequestMapping(value = "/save.json")
    public JsonData saveUser(UserPara para) {
        sysUserService.save(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/update.json")
    public JsonData updateUser(UserPara para) {
        sysUserService.update(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/list.json")
    public JsonData listByDept(@RequestParam("deptId") int deptId, PageQuery page) {
        return JsonData.success(sysUserService.getPageByDeptId(deptId, page));
    }

    @ResponseBody
    @RequestMapping(value = "/tree.json")
    public JsonData tree() {
        return JsonData.success(sysTreeService.userTree());
    }

    @ResponseBody
    @RequestMapping(value = "/query.json")
    public JsonData getRole(@RequestParam("id") int id) {
        return JsonData.success(sysUserService.findById(id));
    }

    @ResponseBody
    @RequestMapping(value = "/setPassword.json")
    public JsonData setPassword(@RequestParam("mail") String mail, @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {
        Preconditions.checkArgument(password.equals(confirmPassword), "两次输入的密码不一致");
        sysUserService.updatePassword(mail, password);
        return JsonData.success();
    }
}
