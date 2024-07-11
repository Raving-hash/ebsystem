package com.raving.ebsystem.modular.system.warpper;

import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.warpper.BaseControllerWarpper;
import com.raving.ebsystem.core.util.ToolUtil;

import java.util.Map;

/**
 * 部门列表的包装
 */
public class DeptWarpper extends BaseControllerWarpper {

    public DeptWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {

        Integer pid = (Integer) map.get("pid");

        if (ToolUtil.isEmpty(pid) || pid.equals(0)) {
            map.put("pName", "--");
        } else {
            map.put("pName", ConstantFactory.me().getDeptName(pid));
        }
    }

}
