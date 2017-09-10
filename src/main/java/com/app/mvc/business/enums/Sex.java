package com.app.mvc.business.enums;

import lombok.Getter;

/**
 * Created by jimin on 16/3/7.
 */
@Getter
public enum Sex {

    Male("M"),
    Female("F");

    private String flag;

    Sex(String flag) {
        this.flag = flag;
    }
}
