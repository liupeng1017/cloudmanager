package com.app.mvc.business.enums;

import lombok.Getter;

/**
 * Created by jimin on 16/3/8.
 */
@Getter
public enum BusinessModelType {
    SELF_SUPPORT("自营"),
    PARTNER_SHIP("合伙"),
    FAMILY("家族"),
    OTHER("其他");

    private String desc;

    BusinessModelType(String desc) {
        this.desc = desc;
    }
}
