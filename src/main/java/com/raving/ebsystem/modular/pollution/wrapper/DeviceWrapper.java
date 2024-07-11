package com.raving.ebsystem.modular.pollution.wrapper;

import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.warpper.BaseControllerWarpper;
import com.raving.ebsystem.core.util.ToolUtil;

import java.util.Map;

public class DeviceWrapper extends BaseControllerWarpper {
    public DeviceWrapper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        Integer deptId = (Integer) map.get("dept");
        if (ToolUtil.isEmpty(deptId)) {
            map.put("deptName", "--");
        } else {
            map.put("deptName", ConstantFactory.me().getDeptName(deptId));
        }
        Long createTime = (Long) map.get("create_time");
        if (ToolUtil.isEmpty(createTime)) {
            map.put("createTime", "--");
        } else {
            map.put("createTime", ConstantFactory.me().getDateTime(createTime));
        }
        Long repairTime = (Long) map.get("repair_time");
        if (ToolUtil.isEmpty(repairTime)) {
            map.put("repairTime", "--");
        } else {
            map.put("repairTime", ConstantFactory.me().getDateTime(repairTime));
        }
    }
}
