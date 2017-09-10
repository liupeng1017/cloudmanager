package com.app.mvc.acl.convert;

import com.app.mvc.acl.domain.SysBase;
import com.app.mvc.acl.domain.SysRole;
import com.app.mvc.acl.vo.RolePara;
import com.app.mvc.exception.ParaException;

/**
 * Created by jimin on 16/1/16.
 */
public class RoleConvert {

    public static SysRole of(RolePara para) throws ParaException {

        BaseConvert.checkPara(para);

        SysBase base = BaseConvert.of();

        return SysRole.builder().id(para.getId()).status(para.getStatus()).name(para.getName()).supplierId(para.getSupplierId()).remark(para.getRemark())
                .type(para.getType()).operator(base.getOperator()).operateIp(base.getOperateIp()).build();
    }
}
