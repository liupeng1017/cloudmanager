package com.app.mvc.business.controller;

import com.app.mvc.beans.JsonData;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.business.service.InvestmentService;
import com.app.mvc.business.vo.InvestmentPara;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by jimin on 16/3/8.
 */
@Slf4j
@Controller
public class InvestmentController {

    @Resource
    private InvestmentService investmentService;

    @ResponseBody
    @RequestMapping("/investment/save.json")
    public JsonData save(InvestmentPara para) {
        investmentService.save(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/admin/investment/list.json")
    public JsonData list(PageQuery page) {
        return JsonData.success(investmentService.getPage(page));
    }

    @ResponseBody
    @RequestMapping("/admin/investment/invalid.json")
    public JsonData invalid(@RequestParam("id") int id) {
        investmentService.invalid(id);
        return JsonData.success();
    }

}
