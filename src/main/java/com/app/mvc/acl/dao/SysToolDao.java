package com.app.mvc.acl.dao;

import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Created by jimin on 16/2/2.
 */
@DBRepository
public interface SysToolDao {

    @Select("SELECT #{url} REGEXP #{rule}")
    int checkRegexp(@Param("url") String url, @Param("rule") String rule);

    @Select("${sql}")
    List<Map> executeSelect(@Param("sql") String sql);

    @Update("${sql}")
    void executeUpdate(@Param("sql") String sql);
}
