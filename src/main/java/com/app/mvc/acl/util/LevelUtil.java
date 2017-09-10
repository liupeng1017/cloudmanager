package com.app.mvc.acl.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by jimin on 16/1/16.
 */

public class LevelUtil {

    // 层级间的分隔符
    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    /**
     * 传入父层级，计算子层级
     *
     * @param parentLevel 父节点的层级
     * @param parentId    父节点的id
     * @return 当前节点的层级
     */
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            // 首层是0
            return String.valueOf(ROOT);
        } else {
            // 非首层：父层level.父层id
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
