package com.app.mvc.acl.dao;

import com.app.mvc.acl.domain.SysAcl;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@DBRepository
public interface SysAclDao {

    void save(SysAcl acl);

    void update(SysAcl acl);

    SysAcl findById(@Param("id") int id);

    SysAcl findByCode(@Param("code") String code);

    List<SysAcl> getByUrlRegexp(@Param("url") String url);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("page") PageQuery page);

    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    int countByNameAndAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("name") String name, @Param("id") Integer id);

    List<SysAcl> getByIdList(@Param("idList") List<Integer> idList);

    List<SysAcl> getAll();

}
