package com.app.mvc.acl.dao;

import com.app.mvc.acl.domain.SysLog;
import com.app.mvc.acl.dto.LogSearchDto;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@DBRepository
public interface SysLogDao {

    void save(SysLog log);

    /**
     * 根据id查询某条记录
     * 用于撤回某次操作
     */
    SysLog findById(@Param("id") int id);

    /**
     * 模糊匹配更新记录
     */
    List<SysLog> fuzzySearch(@Param("dto") LogSearchDto dto, @Param("page") PageQuery page);

    int countFuzzySearch(@Param("dto") LogSearchDto dto);

}
