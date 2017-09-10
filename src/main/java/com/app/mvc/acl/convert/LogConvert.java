package com.app.mvc.acl.convert;

import com.app.mvc.acl.dto.LogSearchDto;
import com.app.mvc.acl.vo.LogPara;

import java.sql.Timestamp;

/**
 * Created by jimin on 16/1/23.
 */
public class LogConvert {

    public static LogSearchDto of(LogPara para) {

        LogSearchDto dto = LogSearchDto.builder().type(para.getType()).beforeSeg(para.getBeforeSeg()).afterSeg(para.getAfterSeg()).targetId(para.getTargetId())
                .operator(para.getOperator()).fromTime(Timestamp.valueOf(para.getFromTime())).toTime(Timestamp.valueOf(para.getToTime())).build();
        return dto;
    }
}
