package com.app.mvc.acl.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by jimin on 16/1/18.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysLog {

    private int id;
    /**
     * log类型
     *
     * @see com.app.mvc.acl.enums.LogType
     */
    private int type;
    /**
     * 更新的行id
     */
    private int targetId;
    /**
     * 更新前数据
     */
    private String oldValue;
    /**
     * 更新后数据
     */
    private String newValue;
    /**
     * 操作者
     */
    private String operator;
    /**
     * 操作者ip
     */
    private String operateIp;
    /**
     * 操作时间
     */
    private Date operateTime;
}
