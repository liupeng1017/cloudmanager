package com.app.mvc.acl.dto;

import com.app.mvc.acl.domain.SysUser;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jimin on 16/2/10.
 */
@Getter
@Setter
public class LoginUser {

    private boolean ret = false;

    private SysUser user;

    private String msg;

    public LoginUser(boolean ret, SysUser user, String msg) {
        this.ret = ret;
        this.user = user;
        this.msg = msg;
    }

    public static LoginUser success(SysUser user) {
        return new LoginUser(true, user, null);
    }

    public static LoginUser fail(String msg) {
        return new LoginUser(false, null, msg);
    }
}
