package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.Device;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface DeviceMapper extends Mapper<Device> {
    void setStatus(Integer deviceId, String status);
    void setDept(Integer deviceId, Integer deptId);

    List<Map<String, Object>> list(String condition);

    List<Map<String, Object>> getDevicesByStatus(String status);


}
