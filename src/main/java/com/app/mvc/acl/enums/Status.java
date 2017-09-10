package com.app.mvc.acl.enums;

import lombok.Getter;

/**
 * Created by jimin on 16/1/16.
 */
@Getter
public enum Status {

    AVAILABLE(1, "有效"),
    NOT_AVAILABLE(0, "无效"),
    DELETED(2, "删除");

    private int code;
    private String desc;

    Status(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
