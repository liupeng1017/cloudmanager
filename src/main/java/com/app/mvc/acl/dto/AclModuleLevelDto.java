package com.app.mvc.acl.dto;

import com.app.mvc.acl.domain.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by jimin on 16/1/24.
 */
@ToString
@Getter
@Setter
public class AclModuleLevelDto extends SysAclModule {

    /**
     * 组装权限模块层级使用
     */
    private List<AclModuleLevelDto> aclModuleList;
    /**
     * 组装权限层级模块下的权限点列表
     */
    private List<AclDto> aclList;

    public static AclModuleLevelDto adapt(SysAclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }
}
