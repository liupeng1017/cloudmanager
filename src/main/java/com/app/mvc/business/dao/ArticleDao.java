package com.app.mvc.business.dao;

import com.app.mvc.beans.PageQuery;
import com.app.mvc.business.domain.Article;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/3/7.
 */
@DBRepository
public interface ArticleDao {

    void save(Article article);

    void update(Article article);

    void incrReadTime(@Param("id") int id);

    Article findById(@Param("id") int id);

    List<Article> getPage(PageQuery page);

    int count();
}
