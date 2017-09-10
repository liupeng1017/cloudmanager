package com.app.mvc.acl.service;

import com.app.mvc.acl.convert.BaseConvert;
import com.app.mvc.acl.convert.UserConvert;
import com.app.mvc.acl.dao.SysUserDao;
import com.app.mvc.acl.domain.SysUser;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.acl.vo.UserPara;
import com.app.mvc.beans.Mail;
import com.app.mvc.beans.PageQuery;
import com.app.mvc.beans.PageResult;
import com.app.mvc.exception.ParaException;
import com.app.mvc.util.MD5Util;
import com.app.mvc.util.MailUtil;
import com.app.mvc.util.PasswordUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@Service
public class SysUserService implements SysService {

    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 新增管理员
     */
    public void save(UserPara para) {
        BaseConvert.checkPara(para);
        para.setSupplierId(RequestHolder.getCurrentUser().getSupplierId());
        SysUser user = UserConvert.of(para);

        if (checkMailExist(para.getMail(), null)) {
            throw new ParaException("邮箱已被占用");
        }
        if (checkTelephoneExist(para.getTelephone(), null)) {
            throw new ParaException("电话已被占用");
        }

        String password = PasswordUtil.randomPassword();
        user.setPassword(MD5Util.encrypt(password));
        sysUserDao.save(user);

        String message = "您的账户已经开通, 请妥善保管好您的个人密码" + password;
        String subject = "用户添加成功通知";
        sendEmailToUser(user, subject, message);

        sysLogService.saveUserLog(null, user);
    }

    /**
     * 更新用户
     */
    public void update(UserPara para) {
        BaseConvert.checkPara(para);
        para.setSupplierId(RequestHolder.getCurrentUser().getSupplierId());
        SysUser before = sysUserDao.findById(para.getId());

        Preconditions.checkNotNull(before, "待更新用户不存在");
        if (checkMailExist(para.getMail(), para.getId())) {
            throw new ParaException("邮箱已被占用");
        }
        if (checkTelephoneExist(para.getTelephone(), para.getId())) {
            throw new ParaException("电话已被占用");
        }

        SysUser after = UserConvert.of(para);
        sysUserDao.update(after);
        sysLogService.saveUserLog(before, after);
    }

    /**
     * 根据用户名或邮箱获取用户信息, 用于登录时校验使用
     */
    public SysUser findByUsernameOrEmail(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return null;
        }
        return sysUserDao.findByUsernameOrEmail(keyword);
    }

    /**
     * 分页获取部门下用户信息
     */
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
        BaseConvert.checkPara(page);
        int count = sysUserDao.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserDao.getPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    public SysUser findById(int id) {
        return sysUserDao.findById(id);
    }

    @Override
    public void recover(int targetId, Object o) {
        SysUser user = (SysUser) o;
        Preconditions.checkNotNull(user, "还原前的用户为空,无法还原");

        SysUser before = sysUserDao.findById(targetId);
        Preconditions.checkNotNull(before, "待还原的用户不存在");

        sysUserDao.update(user);
        sysLogService.saveUserLog(before, user);
    }

    private boolean checkMailExist(String mail, Integer id) {
        return sysUserDao.countByMail(mail, id) > 0;
    }

    private boolean checkTelephoneExist(String telephone, Integer id) {
        return sysUserDao.countByTelephone(telephone, id) > 0;
    }

    public void updatePassword(String mail, String originalPassword) {
        String encryptedPassword = MD5Util.encrypt(originalPassword);
        sysUserDao.updatePassword(mail, encryptedPassword);
    }

    private void sendEmailToUser(SysUser user, String subject, String message) {
        Mail mail = Mail.builder().message(message).receivers(Sets.newHashSet(user.getMail())).subject(subject).build();
        MailUtil.send(mail);
    }

    public List<SysUser> getUserList() {
        int supplierId = RequestHolder.getCurrentUser().getSupplierId();
        return sysUserDao.getBySupplierId(supplierId);
    }
}
