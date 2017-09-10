package com.app.mvc.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by jimin on 16/3/7.
 */
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private Integer id;

    /**
     * 内容
     */
    private String content;
    /**
     * 阅读次数
     */
    private int readTimes;
    /**
     * 标题
     */
    private String title;
    /**
     * 发布时间
     */
    private Date publishTime;
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
