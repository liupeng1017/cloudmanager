package com.app.mvc.acl.convert;

import com.app.mvc.acl.domain.SysAcl;
import com.app.mvc.acl.domain.SysBase;
import com.app.mvc.acl.vo.AclPara;

/**
 * Created by jimin on 16/1/16.
 */
public class AclConvert {

    public static SysAcl of(AclPara para) {

        BaseConvert.checkPara(para);

        SysBase base = BaseConvert.of();

        return SysAcl.builder().id(para.getId()).name(para.getName()).aclModuleId(para.getAclModuleId()).url(para.getUrl()).type(para.getType())
                .status(para.getStatus()).seq(para.getSeq()).remark(para.getRemark()).operator(base.getOperator()).operateIp(base.getOperateIp()).build();
    }
}
