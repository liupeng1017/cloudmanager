package com.app.mvc.business.service;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.domain.SysUser;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.beans.PageResult;
import com.app.mvc.business.dao.InvestmentDao;
import com.app.mvc.business.domain.Investment;
import com.app.mvc.business.vo.InvestmentPara;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jimin on 16/3/8.
 */
@Service
public class InvestmentService {

    @Resource
    private InvestmentDao investmentDao;

    public void save(InvestmentPara para) {
        BaseConvert.checkPara(para);

        Investment investment = Investment.builder().username(para.getUsername()).mobile(para.getMobile()).telephone(para.getTelephone()).fax(para.getFax())
                .area(para.getArea()).sex(para.getSex()).mail(para.getMail()).qq(para.getQq()).businessBrand(para.getBusinessBrand())
                .businessModel(para.getBusinessModel()).venueName(para.getVenueName()).businessSize(para.getBusinessSize()).contractTime(para.getContractTime())
                .investmentAmount(para.getInvestmentAmount()).comment(para.getComment()).build();
        investmentDao.save(investment);
    }

    public void invalid(int id) {
        SysUser user = RequestHolder.getCurrentUser();
        Preconditions.checkNotNull(user);
        investmentDao.invalid(id, user.getUsername());
    }

    public PageResult<Investment> getPage(PageQuery page) {
        BaseConvert.checkPara(page);
        int count = investmentDao.count();
        if (count > 0) {
            List<Investment> list = investmentDao.getPage(page);
            return PageResult.<Investment>builder().total(count).data(list).build();
        }
        return PageResult.<Investment>builder().build();
    }
}
