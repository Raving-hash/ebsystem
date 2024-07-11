package com.raving.ebsystem.modular.pollution.wrapper;

import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.persistence.model.PollutionType;
import com.raving.ebsystem.common.persistence.model.WaterSource;
import com.raving.ebsystem.common.warpper.BaseControllerWarpper;
import com.raving.ebsystem.core.util.ToolUtil;

import java.util.Map;

public class PollutionWrapper extends BaseControllerWarpper {

    public PollutionWrapper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        Integer pollutionTypeId = (Integer) map.get("pollutiontype");
        Integer waterSourceId = (Integer) map.get("watersource");
        long updateTime = (long) map.get("updatetime");
        String waterType = (String) map.get("watertype");
        Double value = (double) map.get("value");

        boolean isOverStandard = ConstantFactory.me().isOverStandard(value, pollutionTypeId);
        if (isOverStandard) {
            map.put("value", value + " (超标)");
        }

        map.put("waterTypeName", ConstantFactory.me().getWaterTypeName(waterType));
        if (ToolUtil.isEmpty(id) || id.equals(0)) {
            map.put("pollTypeName", "--");
            map.put("pollTypeUnit", "--");
        } else {
            PollutionType pollutionType = ConstantFactory.me().getPollutionTypeById(pollutionTypeId);
            if (pollutionType == null) {
                map.put("pollTypeName", "--");
                map.put("pollTypeUnit", "--");
            } else {
                map.put("pollTypeUnit", pollutionType.getUnit());
                map.put("pollTypeName", pollutionType.getName() + "(" + pollutionTypeId + ")");
            }
        }

        if (ToolUtil.isEmpty(waterSourceId) || waterSourceId.equals(0)) {
            map.put("waterSourceName", "--");
        } else {
            WaterSource waterSource = ConstantFactory.me().getWaterSourceById(waterSourceId);
            if (waterSource == null) {
                map.put("waterSourceName", "--");
            } else {
                map.put("waterSourceName", waterSource.getName() + "(" + waterSourceId + ")");
            }
        }

        // 将时间戳转换为时间
        map.put("updatetime", ConstantFactory.me().getDateTime(updateTime));
    }
}
