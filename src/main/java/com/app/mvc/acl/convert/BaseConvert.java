package com.app.mvc.acl.convert;

import com.app.mvc.acl.domain.SysBase;
import com.app.mvc.acl.domain.SysUser;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.exception.ParaException;
import com.app.mvc.util.BeanValidator;
import com.app.mvc.util.IpUtil;
import org.apache.commons.collections.MapUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by jimin on 16/1/16.
 */
public class BaseConvert {

    public static SysBase of() {
        SysUser sysUser = RequestHolder.getCurrentUser();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        return SysBase.builder().operator(sysUser.getUsername()).operateIp(IpUtil.getRemoteIp(request)).build();
    }

    public static void checkPara(Object para) throws ParaException {
        Map<String, String> errors = BeanValidator.validateForObjects(para);
        if (MapUtils.isNotEmpty(errors)) {
            throw new ParaException(errors.toString());
        }
    }
}
