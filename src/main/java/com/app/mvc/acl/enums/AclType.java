package com.app.mvc.acl.enums;

import lombok.Getter;

/**
 * Created by jimin on 16/1/16.
 */
@Getter
public enum AclType {

    MENU(0, "菜单"),
    BUTTON(1, "按钮"),
    OTHER(2, "其他");

    private int code;
    private String desc;

    AclType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
