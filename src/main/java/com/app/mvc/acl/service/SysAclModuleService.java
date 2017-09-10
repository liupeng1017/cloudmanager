package com.app.mvc.acl.service;

import com.app.mvc.acl.convert.AclModuleConvert;
import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.dao.SysAclDao;
import com.app.mvc.acl.dao.SysAclModuleDao;
import com.app.mvc.acl.domain.SysAclModule;
import com.app.mvc.acl.util.LevelUtil;
import com.app.mvc.acl.vo.AclModulePara;
import com.app.mvc.exception.ParaException;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@Service
public class SysAclModuleService implements SysService {

    @Resource
    private SysAclModuleDao sysAclModuleDao;
    @Resource
    private SysAclDao sysAclDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增权限模块
     */
    public void save(AclModulePara para) {
        BaseConvert.checkPara(para);
        SysAclModule aclModule = AclModuleConvert.of(para);

        if (checkExist(para.getParentId(), para.getName(), null)) {
            throw new ParaException("当前层级下存在相同名称的权限模块");
        }

        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(para.getParentId()), para.getParentId()));
        sysAclModuleDao.save(aclModule);
        sysLogService.saveAclModuleLog(null, aclModule);
    }

    /**
     * 更新权限模块
     */
    public void update(AclModulePara para) {
        BaseConvert.checkPara(para);
        SysAclModule before = sysAclModuleDao.findById(para.getId());

        Preconditions.checkNotNull(before, "待更新权限模块不存在");
        Preconditions.checkNotNull(before, "待更新权限模块不存在");

        if (checkExist(para.getParentId(), para.getName(), para.getId())) {
            throw new ParaException("当前层级下存在相同名称的权限模块");
        }

        SysAclModule after = AclModuleConvert.of(para);
        after.setLevel(LevelUtil.calculateLevel(getLevel(para.getParentId()), para.getParentId()));

        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);
    }

    @Transactional
    private void updateWithChild(SysAclModule before, SysAclModule after) {
        sysAclModuleDao.update(after);

        // 获取所有子树
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysAclModule> aclModuleList = sysAclModuleDao.getChildModuleListByLevel(LevelUtil.calculateLevel(before.getLevel(), after.getId()));
            if (CollectionUtils.isEmpty(aclModuleList)) {
                return;
            }
            for (SysAclModule tempModule : aclModuleList) {
                String level = tempModule.getLevel();
                if (level.indexOf(oldLevelPrefix) == 0) {
                    level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                    // 更新每个子树的level
                    tempModule.setLevel(level);
                }
            }
            // 批量更新层级
            sysAclModuleDao.batchUpdateLevel(aclModuleList);
        }
    }

    /**
     * 根据模块id获取对应的level
     */
    public String getLevel(int aclModuleId) {
        SysAclModule aclModule = sysAclModuleDao.findById(aclModuleId);
        return aclModule == null ? null : aclModule.getLevel();
    }

    /**
     * 校验指定模块下是否存在指定名称的权限模块
     *
     * @param aclModuleId 权限模块id
     * @param name        权限模块名称
     * @param id          待排除的权限模块id, 更新时要忽略掉自己
     * @return
     */
    private boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclModuleDao.countByNameAndParentId(aclModuleId, name, id) > 0;
    }

    public SysAclModule findById(int id) {
        return sysAclModuleDao.findById(id);
    }

    @Override
    public void recover(int targetId, Object o) {
        SysAclModule aclModule = (SysAclModule) o;
        Preconditions.checkNotNull(aclModule, "还原前的权限模块为空,无法还原");

        SysAclModule before = sysAclModuleDao.findById(targetId);
        Preconditions.checkNotNull(aclModule, "待还原的权限模块不存在");

        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(aclModule.getParentId()), aclModule.getParentId()));
        sysAclModuleDao.update(aclModule);
        sysLogService.saveAclModuleLog(before, aclModule);
    }

    public void deleteById(int id) {
        SysAclModule aclModule = sysAclModuleDao.findById(id);
        Preconditions.checkNotNull(aclModule, "该权限模块不存在,无法执行删除操作");
        Preconditions.checkArgument(sysAclDao.countByAclModuleId(id) == 0, "权限模块下还有子权限模块,不允许删除!");
        Preconditions.checkArgument(sysAclModuleDao.countByParentId(id) == 0, "权限模块下还有权限点,不允许删除!");
        sysAclModuleDao.deleteById(id);
        sysLogService.saveAclModuleLog(aclModule, null);
    }
}
