package com.app.mvc.business.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by jimin on 16/3/8.
 */
@Getter
@Setter
public class InvestmentPara {

    /**
     * 姓名
     */
    @NotNull(message = "姓名不可以为空")
    private String username;
    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不可以为空")
    private String mobile;
    /**
     * 座机号码
     */
    private String telephone;
    /**
     * 传真
     */
    private String fax;
    /**
     * 所在地区
     */
    @NotNull(message = "所在地区不可以为空")
    private String area;
    /**
     * 性别
     */
    private String sex;
    /**
     * mail
     */
    private String mail;
    /**
     * QQ
     */
    private String qq;
    /**
     * 经营品牌
     */
    @NotNull(message = "经营品牌不可以为空")
    private String businessBrand;
    /**
     * 经营模式
     */
    @NotNull(message = "经营模式不可以为空")
    private String businessModel;
    /**
     * 场馆名称
     */
    private String venueName;
    /**
     * 经营面积
     */
    private String businessSize;
    /**
     * 合约时间
     */
    private String contractTime;
    /**
     * 投资金额
     */
    private String investmentAmount;
    /**
     * 备注
     */
    private String comment;
}
