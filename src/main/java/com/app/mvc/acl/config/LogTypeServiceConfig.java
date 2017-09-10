package com.app.mvc.acl.config;

import com.app.mvc.acl.enums.LogType;
import com.app.mvc.acl.service.SysAclModuleService;
import com.app.mvc.acl.service.SysAclService;
import com.app.mvc.acl.service.SysDeptService;
import com.app.mvc.acl.service.SysRoleAclService;
import com.app.mvc.acl.service.SysRoleService;
import com.app.mvc.acl.service.SysRoleUserService;
import com.app.mvc.acl.service.SysService;
import com.app.mvc.acl.service.SysUserService;
import com.app.mvc.common.SpringHelper;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by jimin on 16/1/24.
 */
public class LogTypeServiceConfig {

    private final static Map<Integer, SysService> logTypeServiceMap = ImmutableMap.<Integer, SysService>builder()
            .put(LogType.ACL.getCode(), SpringHelper.popBean(SysAclService.class))
            .put(LogType.ACL_MODULE.getCode(), SpringHelper.popBean(SysAclModuleService.class))
            .put(LogType.DEPT.getCode(), SpringHelper.popBean(SysDeptService.class))
            .put(LogType.USER.getCode(), SpringHelper.popBean(SysUserService.class))
            .put(LogType.ROLE.getCode(), SpringHelper.popBean(SysRoleService.class))
            .put(LogType.ROLE_USER.getCode(), SpringHelper.popBean(SysRoleUserService.class))
            .put(LogType.ROLE_ACL.getCode(), SpringHelper.popBean(SysRoleAclService.class)).build();

    public static SysService getByLogTypeCode(int code) {
        return logTypeServiceMap.get(code);
    }
}
