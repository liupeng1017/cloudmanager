package com.app.mvc.acl.convert;

import com.app.mvc.acl.domain.SysBase;
import com.app.mvc.acl.domain.SysDept;
import com.app.mvc.acl.vo.DeptPara;

/**
 * Created by jimin on 16/1/16.
 */
public class DeptConvert {

    public static SysDept of(DeptPara para) {

        BaseConvert.checkPara(para);

        SysBase base = BaseConvert.of();

        return SysDept.builder().id(para.getId()).name(para.getName()).seq(para.getSeq()).remark(para.getRemark()).parentId(para.getParentId())
                .operator(base.getOperator()).operateIp(base.getOperateIp()).build();
    }
}
