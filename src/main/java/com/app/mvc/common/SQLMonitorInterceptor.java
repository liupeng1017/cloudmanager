package com.app.mvc.common;

import com.app.mvc.beans.JsonMapper;
import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;
import java.util.Properties;

/**
 * Created by jimin on 15/12/4.
 */

@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
@Slf4j
public class SQLMonitorInterceptor implements Interceptor {

    public SQLMonitorInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }

        String sql;
        try {
            long start = System.currentTimeMillis();
            Object e = invocation.proceed();
            long end = System.currentTimeMillis();
            if (end - start > GlobalConfig.getLongValue(GlobalConfigKey.SLOW_QUERY_MILLSECONDS, 2000)) {
                sql = mappedStatement.getBoundSql(parameter).getSql();
                log.warn("Slow SQL {} millis. sql: {}. parameter: {}", end - start, sql, this.toJson(parameter));
            }

            if (e instanceof Collection && ((Collection) e).size() > GlobalConfig.getIntValue(GlobalConfigKey.SQL_LIST_COUNT, 50)) {
                log.warn("SQL ResultSet {} {} {}  ", mappedStatement.getResource(), mappedStatement.getId(), Integer.valueOf(((Collection) e).size()));
            }
            return e;
        } catch (Exception e) {
            sql = mappedStatement.getBoundSql(parameter).getSql();
            log.error("SQL Error: {}, SQL Parameter: {}", sql, this.toJson(parameter), e);
            throw e;
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

    private String toJson(Object object) {
        if (object == null) {
            return null;
        } else {
            return JsonMapper.obj2String(object);
        }
    }
}
