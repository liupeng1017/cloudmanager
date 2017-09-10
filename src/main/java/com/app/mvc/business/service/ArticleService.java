package com.app.mvc.business.service;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.domain.SysUser;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.beans.PageResult;
import com.app.mvc.business.dao.ArticleDao;
import com.app.mvc.business.domain.Article;
import com.app.mvc.business.vo.ArticlePara;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jimin on 16/3/8.
 */
@Service
public class ArticleService {

    @Resource
    private ArticleDao articleDao;

    public void save(ArticlePara para) {
        BaseConvert.checkPara(para);
        SysUser user = RequestHolder.getCurrentUser();
        Article article = Article.builder().content(para.getContent()).title(para.getTitle()).operator(user.getUsername()).operateIp(user.getOperateIp())
                .build();
        articleDao.save(article);
    }

    public void update(ArticlePara para) {
        BaseConvert.checkPara(para);
        Preconditions.checkNotNull(para.getId());
        SysUser user = RequestHolder.getCurrentUser();
        Article article = Article.builder().content(para.getContent()).title(para.getTitle()).id(para.getId()).operator(user.getUsername())
                .operateIp(user.getOperateIp()).build();
        articleDao.update(article);
    }

    public void incrReadTime(int id) {
        articleDao.incrReadTime(id);
    }

    public PageResult<Article> getPage(PageQuery page) {
        BaseConvert.checkPara(page);
        int count = articleDao.count();
        if (count > 0) {
            List<Article> list = articleDao.getPage(page);
            return PageResult.<Article>builder().data(list).total(count).build();
        }
        return PageResult.<Article>builder().build();
    }
}
