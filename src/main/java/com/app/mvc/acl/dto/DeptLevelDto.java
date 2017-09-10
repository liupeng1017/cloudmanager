package com.app.mvc.acl.dto;

import com.app.mvc.acl.domain.SysDept;
import com.app.mvc.acl.domain.SysUser;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by jimin on 16/1/24.
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
    /**
     * 组装部门层级使用
     */
    private List<DeptLevelDto> deptList = Lists.newArrayList();
    /**
     * 组装部门层级下用户列表
     */
    private List<SysUser> userList = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
