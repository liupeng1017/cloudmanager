package com.app.mvc.acl.enums;

import com.app.mvc.acl.domain.SysAcl;
import com.app.mvc.acl.domain.SysAclModule;
import com.app.mvc.acl.domain.SysDept;
import com.app.mvc.acl.domain.SysRole;
import com.app.mvc.acl.domain.SysUser;
import lombok.Getter;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@Getter
public enum LogType {

    ACL(1, SysAcl.class),
    ACL_MODULE(2, SysAclModule.class),
    USER(3, SysUser.class),
    DEPT(4, SysDept.class),
    ROLE(5, SysRole.class),
    ROLE_USER(6, List.class),
    ROLE_ACL(7, List.class);

    private int code;
    private Class clazz;

    LogType(int code, Class clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public static LogType codeOf(int code) {
        switch (code) {
        case 1:
            return ACL;
        case 2:
            return ACL_MODULE;
        case 3:
            return USER;
        case 4:
            return DEPT;
        case 5:
            return ROLE;
        case 6:
            return ROLE_USER;
        case 7:
            return ROLE_ACL;

        default:
            throw new RuntimeException("unknown log type, code :" + code);
        }
    }
}
