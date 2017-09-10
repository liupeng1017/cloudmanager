package com.app.mvc.acl.service;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.convert.LogConvert;
import com.app.mvc.acl.dao.SysLogDao;
import com.app.mvc.acl.domain.SysAcl;
import com.app.mvc.acl.domain.SysAclModule;
import com.app.mvc.acl.domain.SysBase;
import com.app.mvc.acl.domain.SysDept;
import com.app.mvc.acl.domain.SysLog;
import com.app.mvc.acl.domain.SysRole;
import com.app.mvc.acl.domain.SysUser;
import com.app.mvc.acl.dto.LogSearchDto;
import com.app.mvc.acl.enums.LogType;
import com.app.mvc.acl.vo.LogPara;
import com.app.mvc.beans.JsonMapper;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.beans.PageResult;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@Slf4j
@Service
public class SysLogService {

    @Resource
    private SysLogDao sysLogDao;

    public PageResult<SysLog> getPageByFuzzySearch(LogPara para, PageQuery page) {
        BaseConvert.checkPara(page);
        LogSearchDto dto = LogConvert.of(para);
        int count = sysLogDao.countFuzzySearch(dto);
        if (count > 0) {
            List<SysLog> list = sysLogDao.fuzzySearch(dto, page);
            return PageResult.<SysLog>builder().total(count).data(list).build();
        }
        return PageResult.<SysLog>builder().build();
    }

    public SysLog findById(int id) {
        return sysLogDao.findById(id);
    }

    public void saveAclLog(SysAcl before, SysAcl after) {
        SysBase base = BaseConvert.of();
        SysLog sysLog = SysLog.builder().targetId(after.getId()).type(LogType.ACL.getCode()).oldValue(JsonMapper.obj2String(before))
                .newValue(JsonMapper.obj2String(after)).operator(base.getOperator()).operateIp(base.getOperateIp()).build();
        safetySaveLog(sysLog);
    }

    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysBase base = BaseConvert.of();
        SysLog sysLog = SysLog.builder().targetId(after == null ? before.getId() : after.getId()).type(LogType.ACL_MODULE.getCode())
                .oldValue(JsonMapper.obj2String(before)).newValue(JsonMapper.obj2String(after)).operator(base.getOperator()).operateIp(base.getOperateIp())
                .build();
        safetySaveLog(sysLog);
    }

    public void saveUserLog(SysUser before, SysUser after) {
        SysBase base = BaseConvert.of();
        SysLog sysLog = SysLog.builder().targetId(after.getId()).type(LogType.USER.getCode()).oldValue(JsonMapper.obj2String(before))
                .newValue(JsonMapper.obj2String(after)).operator(base.getOperator()).operateIp(base.getOperateIp()).build();
        safetySaveLog(sysLog);
    }

    public void saveRoleLog(SysRole before, SysRole after) {
        SysBase base = BaseConvert.of();
        SysLog sysLog = SysLog.builder().targetId(after == null ? before.getId() : after.getId()).type(LogType.ROLE.getCode())
                .oldValue(JsonMapper.obj2String(before)).newValue(JsonMapper.obj2String(after)).operator(base.getOperator()).operateIp(base.getOperateIp())
                .build();
        safetySaveLog(sysLog);
    }

    public void saveDeptLog(SysDept before, SysDept after) {
        SysBase base = BaseConvert.of();
        SysLog sysLog = SysLog.builder().targetId(after == null ? before.getId() : after.getId()).type(LogType.DEPT.getCode())
                .oldValue(JsonMapper.obj2String(before)).newValue(JsonMapper.obj2String(after)).operator(base.getOperator()).operateIp(base.getOperateIp())
                .build();
        safetySaveLog(sysLog);
    }

    public void saveRoleAclLog(int roleId, List<Integer> beforeList, List<Integer> afterList) {
        SysBase base = BaseConvert.of();
        SysLog sysLog = SysLog.builder().targetId(roleId).type(LogType.ROLE_ACL.getCode()).oldValue(Joiner.on(",").join(beforeList))
                .newValue(JsonMapper.obj2String(Joiner.on(",").join(afterList))).operator(base.getOperator()).operateIp(base.getOperateIp()).build();
        safetySaveLog(sysLog);
    }

    public void saveRoleUserLog(int roleId, List<Integer> beforeList, List<Integer> afterList) {
        SysBase base = BaseConvert.of();
        SysLog sysLog = SysLog.builder().targetId(roleId).type(LogType.ROLE_USER.getCode()).oldValue(Joiner.on(",").join(beforeList))
                .newValue(JsonMapper.obj2String(Joiner.on(",").join(afterList))).operator(base.getOperator()).operateIp(base.getOperateIp()).build();
        safetySaveLog(sysLog);
    }

    private void safetySaveLog(SysLog sysLog) {
        try {
            sysLogDao.save(sysLog);
        } catch (Throwable e) {
            log.error("add log exception, {}", JsonMapper.obj2String(sysLog), e);
        }
    }
}
