package com.app.mvc.acl.dao;

import com.app.mvc.acl.domain.SysAclModule;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@DBRepository
public interface SysAclModuleDao {

    void save(SysAclModule aclModule);

    void update(SysAclModule aclModule);

    SysAclModule findById(@Param("id") int id);

    List<SysAclModule> getByIdList(@Param("idList") List<Integer> idList);

    List<String> getUniqueLevelByIdList(@Param("idList") List<Integer> idList);

    int countByNameAndParentId(@Param("parentId") int parentId, @Param("name") String name, @Param("id") Integer id);

    List<SysAclModule> getAll();

    int count();

    List<SysAclModule> getChildModuleListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("aclModuleList") List<SysAclModule> aclModuleList);

    int countByParentId(@Param("parentId") int parentId);

    void deleteById(@Param("id") int id);
}
