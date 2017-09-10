package com.app.mvc.acl.service;

import com.app.mvc.acl.dao.SysToolDao;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by jimin on 16/2/2.
 */
@Service
public class SysToolService {

    @Resource
    private SysToolDao sysToolDao;
    @Resource
    private SysCoreService sysCoreService;

    /**
     * 判断url和rule是否满足db层的正则匹配
     */
    public boolean checkRegexp(String url, String rule) {
        Preconditions.checkNotNull(url, "url不可以为空");
        Preconditions.checkNotNull(rule, "rule不可以为空");
        return sysToolDao.checkRegexp(url, rule) == 1;
    }

    public List<Map> executeSelect(String sql) {
        Preconditions.checkArgument(sysCoreService.isRootUser(), "该功能只有root用户可以执行");
        return sysToolDao.executeSelect(sql);
    }

    public void executeUpdate(String sql) {
        Preconditions.checkArgument(sysCoreService.isRootUser(), "该功能只有root用户可以执行");
        sysToolDao.executeUpdate(sql);
    }
}
