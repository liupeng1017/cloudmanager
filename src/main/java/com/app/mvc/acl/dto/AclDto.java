package com.app.mvc.acl.dto;

import com.app.mvc.acl.domain.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * Created by jimin on 16/1/24.
 */
@ToString
@Getter
@Setter
public class AclDto extends SysAcl {

    /**
     * 是否默认选中
     */
    private boolean checked = false;
    /**
     * 是否有权限操作
     */
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }
}
