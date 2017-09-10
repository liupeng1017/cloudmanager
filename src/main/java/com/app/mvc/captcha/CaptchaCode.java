package com.app.mvc.captcha;

import com.app.mvc.acl.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by jimin on 16/3/9.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaCode {

    private Integer id;

    private String code;

    private String sessionId;

    private int status = Status.AVAILABLE.getCode();

    private Date expireTime;

    private int tryTimes = 0;
}
