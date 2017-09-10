package com.app.mvc.captcha;

import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by jimin on 16/3/9.
 */
@DBRepository
public interface CaptchaCodeDao {

    void save(CaptchaCode captchaCode);

    void incrTryTimes(@Param("id") int id);

    CaptchaCode findById(@Param("id") int id);

    CaptchaCode findLastBySessionId(@Param("sessionId") String sessionId);

    int countBySessionIdAndCreateTime(@Param("sessionId") String sessionId, @Param("targetTime") Date targetTime);

    void invalidCaptchaCode(@Param("sessionId") String sessionId, @Param("code") String code);
}
