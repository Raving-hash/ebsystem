package com.raving.ebsystem.modular.pollution.wrapper;

import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.warpper.BaseControllerWarpper;

import java.util.Map;

public class ApplicationWarpper extends BaseControllerWarpper {
    public ApplicationWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        String applyType = (String) map.get("type");
        String typeName = ConstantFactory.me().getApplyTypeName(applyType);
        map.put("typeName", typeName);

        Integer deviceId = (Integer) map.get("device_id");
        String deviceName = ConstantFactory.me().getDeviceName(deviceId);
        map.put("deviceName", deviceName);

        String status = (String) map.get("status");
        String statusName = ConstantFactory.me().getApplyStatusName(status);
        map.put("statusName", statusName);

        Integer userId = (Integer) map.get("user");
        String userName = ConstantFactory.me().getUserNameById(userId);
        map.put("userName", userName);

        Integer auditUserId = (Integer) map.get("audit_user");
        String auditUserName = ConstantFactory.me().getUserNameById(auditUserId);
        map.put("auditUserName", auditUserName);

        map.put("applyTime", ConstantFactory.me().getDateTime((Long) map.get("apply_time")));
        map.put("editTime", ConstantFactory.me().getDateTime((Long) map.get("edit_time")));
        map.put("auditTime", ConstantFactory.me().getDateTime((Long) map.get("audit_time")));
    }
}
