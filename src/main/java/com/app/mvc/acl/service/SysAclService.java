package com.app.mvc.acl.service;

import com.app.mvc.acl.convert.AclConvert;
import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.dao.SysAclDao;
import com.app.mvc.acl.domain.SysAcl;
import com.app.mvc.acl.vo.AclPara;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.beans.PageResult;
import com.app.mvc.exception.ParaException;
import com.app.mvc.util.DateTimeUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@Slf4j
@Service
public class SysAclService implements SysService {

    @Resource
    private SysAclDao sysAclDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增权限点
     */
    public void save(AclPara para) {
        BaseConvert.checkPara(para);
        SysAcl acl = AclConvert.of(para);

        if (checkExist(para.getAclModuleId(), para.getName(), null)) {
            throw new ParaException("当前模块下存在相同名称的权限点");
        }

        acl.setCode(buildCode());
        sysAclDao.save(acl);
        sysLogService.saveAclLog(null, acl);
    }

    /**
     * 更新权限点
     */
    public void update(AclPara para) {
        BaseConvert.checkPara(para);
        SysAcl before = sysAclDao.findById(para.getId());

        Preconditions.checkNotNull(before, "待更新权限点不存在");
        if (checkExist(para.getAclModuleId(), para.getName(), para.getId())) {
            throw new ParaException("当前模块下存在相同名称的权限点");
        }

        SysAcl after = AclConvert.of(para);
        sysAclDao.update(after);
        sysLogService.saveAclLog(before, after);
    }

    private String buildCode() {
        return DateTimeUtil.allFrom(DateTime.now()) + "_" + (int) (Math.random() * 100);
    }

    /**
     * 校验指定模块下是否存在指定名称的权限点
     *
     * @param aclModuleId 权限模块id
     * @param name        权限点名称
     * @param id          待排除的权限点id, 更新时要忽略掉自己
     * @return
     */
    private boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclDao.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    /**
     * 分页获取权限点列表
     */
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery page) {
        BaseConvert.checkPara(page);
        int count = sysAclDao.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> list = sysAclDao.getPageByAclModuleId(aclModuleId, page);
            return PageResult.<SysAcl>builder().total(count).data(list).build();
        } else {
            return PageResult.<SysAcl>builder().build();
        }
    }

    public SysAcl findById(int id) {
        return sysAclDao.findById(id);
    }

    @Override
    public void recover(int targetId, Object o) {
        SysAcl acl = (SysAcl) o;
        Preconditions.checkNotNull(acl, "还原前的权限点为空,无法还原");

        SysAcl before = sysAclDao.findById(targetId);
        Preconditions.checkNotNull(before, "待还原的权限点不存在");

        if (checkExist(acl.getAclModuleId(), acl.getName(), acl.getId())) {
            throw new ParaException("当前模块下存在相同名称的权限点");
        }

        sysAclDao.update(acl);
        sysLogService.saveAclLog(before, acl);
    }

    /**
     * 根据url获取能正则匹配到的权限点列表
     */
    public List<SysAcl> getByUrlRegexp(String url) {
        Preconditions.checkNotNull(url, "url不可以为空");
        return sysAclDao.getByUrlRegexp(url);
    }
}
