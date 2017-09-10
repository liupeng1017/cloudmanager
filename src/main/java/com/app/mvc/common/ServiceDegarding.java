package com.app.mvc.common;

import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by jimin on 16/4/23.
 */
@Slf4j
public class ServiceDegarding {

    public static boolean isAllowed(String url) {
        // 是否为不允许访问的url
        Set<String> notAllowedUrlSet = GlobalConfig.getSetValue(GlobalConfigKey.NOT_ALLOWED_URLS);
        if (notAllowedUrlSet.contains(url)) {
            log.info("service degarding, not allowed to visit url:{}", url);
            return false;
        }
        // 是否为切指定流量访问的url
        Map<String, String> percentAllowedUrlMap = GlobalConfig.getMapValue(GlobalConfigKey.PERCENT_ALLOWED_URLS);
        if (percentAllowedUrlMap.containsKey(url)) {
            String v = percentAllowedUrlMap.get(url);
            int percent = 10; // 如果设置了切部分流量, 默认开放10%的流量, 配置出错时使用这个值
            if (!StringUtils.isNumeric(v)) {
                log.error("service degarding with percent setting error, url:{}, v:{}", v);
            } else {
                percent = Integer.parseInt(v);
            }
            percent = Math.min(percent, 100);
            int random = Math.abs(new Random().nextInt()) % 100;
            log.info("service degarding with percent, url:{}, percent;{}, random:{}", url, percent, random);
            return random < percent;
        }
        return true;
    }
}
