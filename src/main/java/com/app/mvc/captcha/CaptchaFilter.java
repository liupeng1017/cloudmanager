package com.app.mvc.captcha;

import com.app.mvc.beans.JsonData;
import com.app.mvc.beans.JsonMapper;
import com.app.mvc.common.SpringHelper;
import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 在filter里做所有验证码的验证操作
 * Created by jimin on 16/3/9.
 */
@Slf4j
public class CaptchaFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        log.info("captcha filter init config");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Set<String> checkCaptchaUrlSet = GlobalConfig.getSetValue(GlobalConfigKey.CAPTCHA_CODE_VALIDATE_URL);
        if (CollectionUtils.isEmpty(checkCaptchaUrlSet)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        String servletPath = req.getServletPath();
        if (!checkCaptchaUrlSet.contains(servletPath)) {
            chain.doFilter(request, response);
            return;
        }

        String code = req.getParameter("captcha");
        String sessionId = req.getSession().getId();
        log.info("{} need to check captcha code, current: {}, sessionId: {}", servletPath, code, sessionId);
        try {
            CaptchaService captchaService = SpringHelper.popBean(CaptchaService.class);
            boolean validCaptchaCode = captchaService.validCaptchaCode(code, sessionId);
            if (validCaptchaCode) {
                chain.doFilter(request, response);
            } else {
                log.warn("{} validate captcha code failed, current: {}, sessionId: {}", servletPath, code, sessionId);
                captchaService.asyncFailTry(sessionId);
                noAuth(response);
            }
        } catch (Throwable t) {
            log.error("{} validate captcha code exception, {}", servletPath, t);
            noAuth(response);
        }
        return;
    }

    public void noAuth(ServletResponse resp) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;
        String contentType = "application/json";
        response.setHeader("Content-Type", contentType + "; charset=UTF-8");
        JsonData result = JsonData.error("验证码错误!");
        response.getWriter().print(JsonMapper.obj2String(result));
    }

    @Override
    public void destroy() {
        log.info("captcha filter destroy");
    }
}
