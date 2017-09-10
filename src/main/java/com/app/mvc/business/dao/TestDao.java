package com.app.mvc.business.dao;

import com.app.mvc.business.domain.Test;
import com.app.mvc.common.DBRepository;

/**
 * Created by jimin on 16/3/22.
 */
@DBRepository
public interface TestDao {

    void save(Test test);
}
