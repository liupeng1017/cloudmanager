package com.app.mvc.business.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by jimin on 16/3/7.
 */
@Getter
@Setter
public class ProductPara {

    private Integer id;

    @NotNull(message = "标题不可以为空")
    private String title;

    @NotNull(message = "产品图片不可以为空")
    private String image;
}
