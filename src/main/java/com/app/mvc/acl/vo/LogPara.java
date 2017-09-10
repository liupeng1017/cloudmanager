package com.app.mvc.acl.vo;

import com.app.mvc.beans.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jimin on 16/1/23.
 */
@ToString
@Getter
@Setter
public class LogPara extends PageQuery {
    /**
     * log类型
     *
     * @see com.app.mvc.acl.enums.LogType
     */
    private Integer type;
    /**
     * 指定的id
     */
    private Integer targetId;
    /**
     * 原值
     */
    private String beforeSeg;
    /**
     * 新值
     */
    private String afterSeg;
    /**
     * 更新人
     */
    private String operator;
    /**
     * 开始时间
     * <code>yyyy-[m]m-[d]d hh:mm:ss[.f...]</code>
     */
    private String fromTime;
    /**
     * 结束时间
     * <code>yyyy-[m]m-[d]d hh:mm:ss[.f...]</code>
     */
    private String toTime;
}
