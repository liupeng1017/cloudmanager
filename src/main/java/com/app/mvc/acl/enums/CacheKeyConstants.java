package com.app.mvc.acl.enums;

import lombok.Getter;

/**
 * 缓存的前缀, 统一管理
 * Created by jimin on 16/2/4.
 */
@Getter
public enum CacheKeyConstants {

    /**
     * 缓存用户的信息
     */
    USERID_USER,

    /**
     * 缓存用户的信息
     */
    USERNAME_USER,

    /**
     * 缓存用户拥有的权限点
     */
    USERID_ACLLIST,

    /**
     * 缓存用户拥有的权限树
     */
    USERID_ACLTREE,

    /**
     * 缓存用户校验过的权限点,可能有权限,可能无权限
     */
    USERID_ACLCODE,

    /**
     * 缓存用户校验过的url,可能有权限,可能无权限
     */
    USERID_URL,

    /**
     * 缓存系统中校验的url
     */
    SYS_URL_LIST

}
