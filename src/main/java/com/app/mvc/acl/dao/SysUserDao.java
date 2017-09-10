package com.app.mvc.acl.dao;

import com.app.mvc.acl.domain.SysUser;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@DBRepository
public interface SysUserDao {

    void save(SysUser sysUser);

    void update(SysUser sysUser);

    void updatePassword(@Param("mail") String mail, @Param("password") String password);

    SysUser findById(@Param("id") int id);

    SysUser findByUsernameOrEmail(@Param("keyword") String keyword);

    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);

    int countByDeptId(@Param("deptId") int deptId);

    int countAvailableByDeptId(@Param("deptId") int deptId);

    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    List<SysUser> getBySupplierId(@Param("supplierId") int supplierId);
}
