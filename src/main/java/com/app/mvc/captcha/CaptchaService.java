package com.app.mvc.captcha;

import com.app.mvc.acl.enums.Status;
import com.app.mvc.common.ThreadPool;
import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by jimin on 16/3/9.
 */
@Slf4j
@Service
public class CaptchaService {

    @Resource
    private CaptchaCodeDao captchaCodeDao;

    public void saveCaptchaCode(String code, String sessionId) {
        Preconditions.checkNotNull(code, "数据非法");
        Preconditions.checkArgument(canGenerate(sessionId), "请求次数过于频繁,请稍后重试");
        Date expireTime = DateUtils.addMinutes(new Date(), GlobalConfig.getIntValue(GlobalConfigKey.CAPTCHA_CODE_INVALID_MINUTES, 5));
        CaptchaCode captchaCode = CaptchaCode.builder().code(code).sessionId(sessionId).expireTime(expireTime).build();
        captchaCodeDao.save(captchaCode);
    }

    public boolean validCaptchaCode(final String code, final String sessionId) {
        Preconditions.checkNotNull(code);
        Preconditions.checkNotNull(sessionId);
        CaptchaCode captchaCode = captchaCodeDao.findLastBySessionId(sessionId);
        if (captchaCode == null || captchaCode.getExpireTime().getTime() < System.currentTimeMillis() || captchaCode.getStatus() != Status.AVAILABLE
                .getCode()) {
            return false;
        }
        if (captchaCode.getCode().equalsIgnoreCase(code)) {
            // 一旦验证码验证通过,则将其置为不可用状态,之后不再允许被使用
            // 如果后面需要使用,则须生成新的验证码
            asyncInvalidCaptchaCode(code, sessionId);
            return true;
        }
        return false;
    }

    /**
     * 检测一段时间内该session生成验证码的数量是否超出正常范围
     */
    private boolean canGenerate(String sessionId) {
        Preconditions.checkNotNull(sessionId, "数据非法");
        Date targetDate = DateUtils.addMinutes(new Date(), -1);
        int maxTimes = GlobalConfig.getIntValue(GlobalConfigKey.CAPTCHA_CODE_ONE_MINUTE_MAX, 2);
        return captchaCodeDao.countBySessionIdAndCreateTime(sessionId, targetDate) < maxTimes;
    }

    private void asyncInvalidCaptchaCode(final String code, final String sessionId) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    captchaCodeDao.invalidCaptchaCode(sessionId, code);
                } catch (Throwable e) {
                    log.error("invalid captcha code error, code: {}, sessionId: {}", code, sessionId, e);
                }
            }
        });
    }

    public void asyncFailTry(final String sessionId) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    failTry(sessionId);
                } catch (Throwable e) {
                    log.error("update captcha code tryTimes error, sessionId: {}", sessionId, e);
                }
            }
        });
    }

    private void failTry(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return;
        }
        CaptchaCode captchaCode = captchaCodeDao.findLastBySessionId(sessionId);
        if (captchaCode == null || captchaCode.getExpireTime().getTime() < System.currentTimeMillis() || captchaCode.getStatus() != Status.AVAILABLE
                .getCode()) {
            return;
        }
        captchaCodeDao.incrTryTimes(captchaCode.getId());
    }
}
