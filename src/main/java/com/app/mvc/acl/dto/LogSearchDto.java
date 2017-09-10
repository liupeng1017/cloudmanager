package com.app.mvc.acl.dto;

import com.app.mvc.beans.PageQuery;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * Created by jimin on 16/1/23.
 */
@ToString
@Getter
@Setter
@Builder
public class LogSearchDto extends PageQuery {
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
     */
    private Timestamp fromTime;
    /**
     * 结束时间
     */
    private Timestamp toTime;
}
