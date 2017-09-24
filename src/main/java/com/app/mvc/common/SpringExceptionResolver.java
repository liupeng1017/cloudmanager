package com.app.mvc.common;

import com.app.mvc.beans.JsonData;
import com.app.mvc.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    private static final String JSON_URI = ".json";

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String msg = "system error";

        if (ex instanceof NotFoundException) {
            JsonData result = JsonData.error(ex.getMessage());
            mv = new ModelAndView("exception", result.toMap());
        } else if (url.endsWith(JSON_URI)) {
            if (ex instanceof RuntimeException) {
                JsonData result = JsonData.error(ex.getMessage());
                mv = new ModelAndView("jsonView", result.toMap());
            } else {
                JsonData result = JsonData.error(msg);
                mv = new ModelAndView("jsonView", result.toMap());
            }
        } else {
            log.error("系统错误", ex);
            JsonData result = JsonData.error(msg);
            mv = new ModelAndView("exception", result.toMap());
        }

        log.error("SpringMVCException, url=" + url + ", msg=" + ex.getMessage(), ex);
        return mv;
    }
}
