package com.app.mvc.acl.service;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.convert.DeptConvert;
import com.app.mvc.acl.dao.SysDeptDao;
import com.app.mvc.acl.dao.SysUserDao;
import com.app.mvc.acl.domain.SysDept;
import com.app.mvc.acl.util.LevelUtil;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.acl.vo.DeptPara;
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
public class SysDeptService implements SysService {

    @Resource
    private SysDeptDao sysDeptDao;
    @Resource
    private SysLogService sysLogService;
    @Resource
    private SysUserDao sysUserDao;

    /**
     * 新增部门
     */
    public void save(DeptPara para) {
        BaseConvert.checkPara(para);
        SysDept dept = DeptConvert.of(para);
        if (checkExist(para.getParentId(), para.getName(), null)) {
            throw new ParaException("同一层级下存在相同名称的部门");
        }
        dept.setLevel(LevelUtil.calculateLevel(getLevel(para.getParentId()), para.getParentId()));
        sysDeptDao.save(dept);
        sysLogService.saveDeptLog(null, dept);
    }

    /**
     * 更新部门
     */
    public void update(DeptPara para) {
        BaseConvert.checkPara(para);
        SysDept before = sysDeptDao.findById(para.getId());

        Preconditions.checkNotNull(before, "待更新部门不存在");
        if (checkExist(para.getParentId(), para.getName(), para.getId())) {
            throw new ParaException("当前模块下存在相同名称的权限点");
        }
        SysDept after = DeptConvert.of(para);
        after.setLevel(LevelUtil.calculateLevel(getLevel(para.getParentId()), para.getParentId()));

        updateWithChild(before, after);
        sysLogService.saveDeptLog(before, after);
    }

    @Transactional
    private void updateWithChild(SysDept before, SysDept after) {
        sysDeptDao.update(after);

        // 获取所有子树
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptDao.getChildDeptListByLevel(LevelUtil.calculateLevel(before.getLevel(), after.getId()));
            if (CollectionUtils.isEmpty(deptList)) {
                return;
            }
            for (SysDept tempDept : deptList) {
                String level = tempDept.getLevel();
                if (level.indexOf(oldLevelPrefix) == 0) {
                    level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                    // 更新每个子树的level
                    tempDept.setLevel(level);
                }
            }
            // 批量更新层级
            sysDeptDao.batchUpdateLevel(deptList);
        }
    }

    public String getLevel(int deptId) {
        SysDept dept = sysDeptDao.findById(deptId);
        return dept == null ? null : dept.getLevel();
    }

    /**
     * 获取部门列表
     */
    public List<SysDept> getBySupplierId(int supplierId) {
        return sysDeptDao.getBySupplierId(supplierId);
    }

    /**
     * 获取用户所在供应商的部门列表
     */
    public List<SysDept> list() {
        int userSupplierId = RequestHolder.getCurrentUser().getSupplierId();
        return getBySupplierId(userSupplierId);
    }

    public SysDept findById(int id) {
        return sysDeptDao.findById(id);
    }

    public void deleteById(int id) {
        SysDept dept = sysDeptDao.findById(id);
        Preconditions.checkNotNull(dept, "该部门不存在,无法执行删除操作");
        Preconditions.checkArgument(sysUserDao.countAvailableByDeptId(id) == 0, "部门下还有用户,不允许删除!");
        Preconditions.checkArgument(sysDeptDao.countByParentId(id) == 0, "部门下还有子部门,不允许删除!");
        sysDeptDao.deleteById(id);
        sysLogService.saveDeptLog(dept, null);
    }

    @Override
    public void recover(int targetId, Object o) {
        SysDept dept = (SysDept) o;
        Preconditions.checkNotNull(dept, "还原前的部门为空,无法还原");

        SysDept before = sysDeptDao.findById(targetId);
        Preconditions.checkNotNull(before, "待还原的部门不存在");

        sysDeptDao.update(dept);
        sysLogService.saveDeptLog(before, dept);
    }

    private boolean checkExist(int deptId, String name, Integer id) {
        return sysDeptDao.countByNameAndParentId(deptId, name, id) > 0;
    }
}
