package com.app.mvc.acl.service;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.convert.RoleConvert;
import com.app.mvc.acl.dao.SysRoleAclDao;
import com.app.mvc.acl.dao.SysRoleDao;
import com.app.mvc.acl.dao.SysRoleUserDao;
import com.app.mvc.acl.domain.SysRole;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.acl.vo.RolePara;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@Service
public class SysRoleService implements SysService {

    @Resource
    private SysRoleDao sysRoleDao;
    @Resource
    private SysRoleAclDao sysRoleAclDao;
    @Resource
    private SysRoleUserDao sysRoleUserDao;
    @Resource
    private SysLogService sysLogService;

    public void save(RolePara para) {
        BaseConvert.checkPara(para);
        SysRole role = RoleConvert.of(para);

        sysRoleDao.save(role);
        sysLogService.saveRoleLog(null, role);
    }

    public void update(RolePara para) {
        BaseConvert.checkPara(para);
        SysRole before = sysRoleDao.findById(para.getId());

        Preconditions.checkNotNull(before, "待更新角色不存在");

        SysRole after = RoleConvert.of(para);
        sysRoleDao.update(after);
        sysLogService.saveRoleLog(before, after);
    }

    public List<SysRole> getBySupplierId(int supplierId) {
        return sysRoleDao.getBySupplierId(supplierId);
    }

    public List<SysRole> list() {
        int userSupplierId = RequestHolder.getCurrentUser().getSupplierId();
        return getBySupplierId(userSupplierId);
    }

    public SysRole findById(int id) {
        return sysRoleDao.findById(id);
    }

    @Override
    public void recover(int targetId, Object o) {
        SysRole role = (SysRole) o;
        Preconditions.checkNotNull(role, "还原前的角色为空,无法还原");

        SysRole before = sysRoleDao.findById(targetId);
        Preconditions.checkNotNull(before, "待还原的角色不存在");

        sysRoleDao.update(role);
        sysLogService.saveRoleLog(before, role);
    }

    public void deleteById(int id) {
        SysRole role = findById(id);
        Preconditions.checkNotNull(role, "该角色不存在,无法执行删除操作");
        Preconditions.checkArgument(sysRoleAclDao.countByRoleId(id) == 0, "该角色下还有权限点,不允许删除!");
        Preconditions.checkArgument(sysRoleUserDao.countByRoleId(id) == 0, "该角色下还有用户,不允许删除!");
        sysRoleDao.deleteById(id);
        sysLogService.saveRoleLog(role, null);
    }
}
