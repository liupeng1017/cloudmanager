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
public class Investment {

    private Integer id;

    private String username;

    private String mobile;

    private String telephone;

    private String fax;

    private String area;

    private String sex;

    private String mail;

    private String qq;

    private String businessBrand;

    private String businessModel;

    private String venueName;

    private String businessSize;

    private String contractTime;

    private String investmentAmount;

    private String comment;

    private Date createTime;

    private int status;

    private String operator;

    private Date operateTime;
}
