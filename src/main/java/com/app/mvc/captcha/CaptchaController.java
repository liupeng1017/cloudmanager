package com.app.mvc.captcha;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jimin on 16/3/9.
 */
@Slf4j
@Controller
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @RequestMapping("/captcha")
    public void generate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        Captcha captcha = new Captcha(120, 40, 5, 30);
        String sessionId = request.getSession().getId();
        try {
            captchaService.saveCaptchaCode(captcha.getCode(), sessionId);
        } catch (Throwable t) {
            log.error("保存验证码出错, 不影响生成的验证码, 但本次验证也会出错, code:{}, sessionId:{}", captcha.getCode(), sessionId, t);
        }
        try {
            captcha.write(response.getOutputStream());
        } catch (Throwable t) {
            log.error("验证码返回出现异常, code:{}, sessionId:{}", captcha.getCode(), sessionId, t);
            throw Throwables.propagate(t);
        }
    }
}
