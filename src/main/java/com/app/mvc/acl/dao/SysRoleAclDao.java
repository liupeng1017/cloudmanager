package com.app.mvc.acl.dao;

import com.app.mvc.acl.domain.SysRoleAcl;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@DBRepository
public interface SysRoleAclDao {

    List<Integer> getAclIdListByRoleId(@Param("roleId") int roleId);

    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void deleteByRoleId(@Param("roleId") int roleId);

    void batchInsert(@Param("list") List<SysRoleAcl> list);

    int countByRoleId(@Param("roleId") int roleId);
}
