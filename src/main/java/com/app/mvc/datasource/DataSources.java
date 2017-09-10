package com.app.mvc.datasource;

import lombok.Getter;

/**
 * Created by jimin on 16/3/21.
 */
@Getter
public enum DataSources {
    MASTER("db-master"),
    LOG("db-log");

    private String key;

    DataSources(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
