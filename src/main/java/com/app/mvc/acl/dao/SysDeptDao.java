package com.app.mvc.acl.dao;

import com.app.mvc.acl.domain.SysDept;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@DBRepository
public interface SysDeptDao {

    void save(SysDept sysDept);

    void update(SysDept sysDept);

    SysDept findById(@Param("id") int id);

    void deleteById(@Param("id") int id);

    List<SysDept> getBySupplierId(@Param("supplierId") int supplierId);

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("deptList") List<SysDept> deptList);

    int countByParentId(@Param("id") int id);

    int countByNameAndParentId(@Param("parentId") int parentId, @Param("name") String name, @Param("id") Integer id);
}
