package com.app.mvc.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jimin on 16/3/27.
 */
@Controller
@RequestMapping("/admin")
public class NavigatorController {

    @RequestMapping("/page.do")
    public ModelAndView toAdmin() {
        return new ModelAndView("admin");
    }
}
