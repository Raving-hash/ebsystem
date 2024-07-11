package com.raving.ebsystem.modular.pollution.wrapper;

import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.warpper.BaseControllerWarpper;

import java.util.Map;

public class WarnWaterWrapper extends BaseControllerWarpper {

    public WarnWaterWrapper(Object obj) {
        super(obj);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
       Integer waterSourceId = (Integer) map.get("water_source_id");
         if (waterSourceId == null) {
              map.put("waterSourceName", "--");
         } else {
              map.put("waterSourceName", ConstantFactory.me().getWaterSourceById(waterSourceId).getName());
         }
    }
}
