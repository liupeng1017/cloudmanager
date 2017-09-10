package com.app.mvc.acl.filter;

import com.app.mvc.acl.domain.SysUser;
import com.app.mvc.acl.service.SysCoreService;
import com.app.mvc.acl.util.RequestHolder;
import com.app.mvc.beans.JsonMapper;
import com.app.mvc.beans.Result;
import com.app.mvc.common.SpringHelper;
import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by jimin on 16/2/6.
 */
@Slf4j
public class AclControlFilter implements Filter {

    private static Set<String> exclusionUrlSet = new ConcurrentSkipListSet<String>();
    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig config) throws ServletException {
        // 获取权限校验范围内的白名单url
        String exclusionUrls = config.getInitParameter("exclusionUrls");
        String[] exclusionUrlArray = StringUtils.split(exclusionUrls, ",");
        for (String exclusionUrl : exclusionUrlArray) {
            exclusionUrlSet.add(exclusionUrl.trim());
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String servletPath = request.getServletPath();
        Map requestMap = request.getParameterMap();

        // 权限校验范围内的白名单URL不过滤
        if (exclusionUrlSet.contains(servletPath)) {
            chain.doFilter(req, resp);
            return;
        }

        Set<String> whiteList = GlobalConfig.getSetValue(GlobalConfigKey.ACCESS_WHITELIST);
        whiteList.add(getNoAuthPage());
        for (String ignoreUrlPattern : whiteList) {
            if (pathMatcher.match(ignoreUrlPattern, servletPath)) {
                chain.doFilter(req, resp);
                return;
            }
        }

        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {
            log.info("someone visit {}, but not login, need to check the url setting. parameter:{}", servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;
        }

        SysCoreService sysCoreService = SpringHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAclFromCache(servletPath)) {
            log.info("{} visit {}, but no auth. parameter:{}", sysUser.getUsername(), servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;
        }
        log.info("{} visit {}, succeed. parameter:{}", sysUser.getUsername(), servletPath, JsonMapper.obj2String(requestMap));

        chain.doFilter(req, resp);
        return;
    }

    /**
     * 无权处理
     */
    public void noAuth(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        request.setAttribute("currentUrl", servletPath);
        String requestAccept = request.getHeader("accept");
        String contentType = "text/html";
        // 无权限访问的页面
        String noAuthPage = getNoAuthPage();

        //判断请求类型
        if (StringUtils.isNotEmpty(requestAccept)) {
            if (StringUtils.contains(requestAccept, "application/json") || StringUtils.contains(requestAccept, "text/javascript") || StringUtils
                    .contains(requestAccept, "text/json")) {
                contentType = "application/json";
            }
        }
        response.setHeader("Content-Type", contentType + "; charset=UTF-8");
        if (contentType.equals("text/html")) {
            clientRedirect(response, noAuthPage);
        } else if (contentType.equals("application/json")) {
            Result result = Result.fail("没有访问权限，如需要访问，请联系管理员！");
            response.getWriter().print(JsonMapper.obj2String(result));
        } else {
            clientRedirect(response, noAuthPage);
        }
    }

    private String getNoAuthPage() {
        return GlobalConfig.getStringValue(GlobalConfigKey.NO_AUTH_PAGE, "/sys/user/noAuth.do");
    }

    /**
     * 客户端的重定向，将页面重定向为 path?ret=encodeURIComponent(window.location.href)
     */
    public static void clientRedirect(HttpServletResponse response, String path) throws IOException {
        response.setHeader("Content-Type", "text/html");
        response.getWriter().println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + path + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }

    @Override
    public void destroy() {
    }
}
