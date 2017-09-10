package com.app.mvc.business.controller;

import com.app.mvc.beans.JsonData;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.business.service.ArticleService;
import com.app.mvc.business.vo.ArticlePara;
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
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @ResponseBody
    @RequestMapping("/admin/article/save.json")
    public JsonData save(ArticlePara para) {
        articleService.save(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/admin/article/update.json")
    public JsonData update(ArticlePara para) {
        articleService.update(para);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/article/read.json")
    public JsonData read(@RequestParam("id") int id) {
        articleService.incrReadTime(id);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/article/list.json")
    public JsonData read(PageQuery page) {
        return JsonData.success(articleService.getPage(page));
    }
}
