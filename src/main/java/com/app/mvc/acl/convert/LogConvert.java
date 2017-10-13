package com.app.mvc.acl.convert;

import com.app.mvc.acl.dto.LogSearchDto;
import com.app.mvc.acl.vo.LogPara;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

/**
 * Created by jimin on 16/1/23.
 */
public class LogConvert {

    public static LogSearchDto of(LogPara para) {

        LogSearchDto dto = LogSearchDto.builder()
                .type(para.getType())
                .beforeSeg(StringUtils.isBlank(para.getBeforeSeg()) ? null : "%" + para.getBeforeSeg() + "%")
                .afterSeg(StringUtils.isBlank(para.getAfterSeg()) ? null : "%" + para.getAfterSeg() + "%")
                .operator(StringUtils.isBlank(para.getOperator()) ? null : "%" + para.getOperator() + "%")
                .fromTime(StringUtils.isBlank(para.getFromTime()) ? null : Timestamp.valueOf(para.getFromTime()))
                .toTime(StringUtils.isBlank(para.getToTime()) ? null : Timestamp.valueOf(para.getToTime()))
                .build();
        return dto;
    }
}
