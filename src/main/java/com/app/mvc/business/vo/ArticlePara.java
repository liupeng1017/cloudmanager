package com.app.mvc.business.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by jimin on 16/3/8.
 */
@Getter
@Setter
public class ArticlePara {

    private Integer id;
    /**
     * 内容
     */
    @NotNull(message = "内容不可以为空")
    private String content;
    /**
     * 标题
     */
    @NotNull(message = "标题不可以为空")
    private String title;
}
