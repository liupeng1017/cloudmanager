package com.app.mvc.acl.dao;

import com.app.mvc.acl.domain.SysRoleUser;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@DBRepository
public interface SysRoleUserDao {

    List<Integer> getUserIdListByRoleId(@Param("roleId") int roleId);

    List<Integer> getRoleIdListByUserId(@Param("userId") int userId);

    void deleteByRoleId(@Param("roleId") int roleId);

    void batchInsert(@Param("list") List<SysRoleUser> list);

    int countByRoleId(@Param("roleId") int roleId);
}
