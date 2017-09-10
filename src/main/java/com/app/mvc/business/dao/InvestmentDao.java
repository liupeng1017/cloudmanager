package com.app.mvc.business.dao;

import com.app.mvc.beans.PageQuery;
import com.app.mvc.business.domain.Investment;
import com.app.mvc.common.DBRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jimin on 16/3/8.
 */
@DBRepository
public interface InvestmentDao {

    void save(Investment investment);

    void invalid(@Param("id") int id, @Param("operator") String operator);

    int count();

    List<Investment> getPage(PageQuery page);
}
