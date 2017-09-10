package com.app.mvc.acl.enums;

import lombok.Getter;

/**
 * Created by jimin on 16/1/16.
 */
@Getter
public enum RoleType {

    ADMIN(0, "管理员角色"),
    SUPPLIER(1, "供应商角色"),
    OTHER(2, "备用角色");

    private int code;
    private String desc;

    RoleType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
