package com.app.mvc.shortUrl;

import com.app.mvc.beans.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jimin on 16/4/7.
 */
@Slf4j
@Controller
public class ShortUrlController {

    @Resource
    private ShortUrlService shortUrlService;

    @ResponseBody
    @RequestMapping("/gen")
    public JsonData generate(@RequestParam("url") String url) {
        return JsonData.success(shortUrlService.generate(url));
    }

    @RequestMapping("/t/{url}")
    public void toOrigin(@PathVariable("url") String url, HttpServletResponse response) {
        try {
            response.sendRedirect(shortUrlService.getOriginUrlWithoutException(url));
        } catch (Throwable t) {
            log.error("根据短链接跳转出现异常, url=" + url, t);
        }
    }
}
