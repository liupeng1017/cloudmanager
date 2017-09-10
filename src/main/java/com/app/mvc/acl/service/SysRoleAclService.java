package com.app.mvc.acl.service;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.dao.SysAclDao;
import com.app.mvc.acl.dao.SysRoleAclDao;
import com.app.mvc.acl.domain.SysAcl;
import com.app.mvc.acl.domain.SysBase;
import com.app.mvc.acl.domain.SysRoleAcl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created by jimin on 16/1/23.
 */
@Service
public class SysRoleAclService implements SysService {

    @Resource
    private SysRoleAclDao sysRoleAclDao;
    @Resource
    private SysAclDao sysAclDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 更新角色-权限点关系
     */
    public void changeRoleAcls(int roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = sysRoleAclDao.getAclIdListByRoleId(roleId);
        if (originAclIdList.size() == aclIdList.size()) { // 如果调整后的长度和调整前的长度相同，检查一下是否没有调整直接做更新，这时不做更新
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) { // 说明修改前后是一样的，那就不进行操作了
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
        sysLogService.saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }

    @Transactional
    private void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        // 删除旧的
        sysRoleAclDao.deleteByRoleId(roleId);

        // 组装新的
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        SysBase base = BaseConvert.of();
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {
            roleAclList.add(SysRoleAcl.builder().roleId(roleId).aclId(aclId).operator(base.getOperator()).operateIp(base.getOperateIp()).build());
        }

        // 添加新的
        sysRoleAclDao.batchInsert(roleAclList);
    }

    public List<SysAcl> getListByRoleId(int roleId) {
        List<Integer> aclIdList = sysRoleAclDao.getAclIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclDao.getByIdList(aclIdList);
    }

    @Override
    public void recover(int targetId, Object o) {
        List<Integer> aclIdList = (List<Integer>) o;
        changeRoleAcls(targetId, aclIdList);
    }
}
