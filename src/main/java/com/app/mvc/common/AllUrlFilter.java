package com.app.mvc.common;

import com.app.mvc.beans.JsonMapper;
import com.app.mvc.beans.Result;
import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

/**
 * Created by jimin on 16/4/23.
 */
@Slf4j
public class AllUrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("all url filter init config");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String servletPath = req.getServletPath();

        // 系统降级处理
        if (!ServiceDegarding.isAllowed(servletPath)) {
            Map requestMap = request.getParameterMap();
            log.info("{} service degarding. parameter:{}", servletPath, JsonMapper.obj2String(requestMap));
            serviceDegardingPage(req, resp);
            return;
        }

        // URL QPS 限制
        QPSLimiter.acquire(1, servletPath);

        // 其他处理,等待补充

        chain.doFilter(req, resp);
        return;
    }

    /**
     * 服务降级页面
     */
    public void serviceDegardingPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        request.setAttribute("currentUrl", servletPath);
        String requestAccept = request.getHeader("accept");
        String contentType = "text/html";
        // 无权限访问的页面
        String noAuthPage = getServiceDegardingPage();

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
            Result result = Result.fail("服务降级,请稍后再试");
            response.getWriter().print(JsonMapper.obj2String(result));
        } else {
            clientRedirect(response, noAuthPage);
        }
    }

    private String getServiceDegardingPage() {
        return GlobalConfig.getStringValue(GlobalConfigKey.SERVICE_DEGARDING_PAGE, "/serviceDegarding.do");
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
        log.info("all url filter destroy");
    }
}
