package com.raving.ebsystem.common.constant.dictmap;

import com.raving.ebsystem.common.constant.dictmap.base.AbstractDictMap;

/**
 * 审核申请的映射
 */
public class ApplicationDict extends AbstractDictMap {
    @Override
    public void init() {
        put("applicationId","审核申请");
        put("auditUser", "审核人");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("deviceId", "getDeviceName");
        putFieldWrapperMethodName("applyTime", "getDateTime");
        putFieldWrapperMethodName("deviceId", "getDeviceName");
        putFieldWrapperMethodName("auditUser", "getUserNameById");
    }
}
