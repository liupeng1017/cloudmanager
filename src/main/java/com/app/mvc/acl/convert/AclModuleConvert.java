package com.app.mvc.acl.convert;

import com.app.mvc.acl.domain.SysAclModule;
import com.app.mvc.acl.domain.SysBase;
import com.app.mvc.acl.vo.AclModulePara;

/**
 * Created by jimin on 16/1/16.
 */
public class AclModuleConvert {

    public static SysAclModule of(AclModulePara para) {

        BaseConvert.checkPara(para);

        SysBase base = BaseConvert.of();

        return SysAclModule.builder().id(para.getId()).name(para.getName()).parentId(para.getParentId()).status(para.getStatus()).seq(para.getSeq())
                .remark(para.getRemark()).operator(base.getOperator()).operateIp(base.getOperateIp()).build();
    }
}
