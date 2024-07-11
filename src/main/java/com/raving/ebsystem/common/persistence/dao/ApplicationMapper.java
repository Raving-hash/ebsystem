package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.Application;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ApplicationMapper extends Mapper<Application> {
    void setStatus(Integer applicationId, String status);
    List<Map<String, Object>> getApplicationsByStatus(String status);

    List<Map<String, Object>> getApplicationsByDept(Integer deviceId);
    List<Map<String, Object>> getApplicationsByUser(Integer userId);

    List<Map<String, Object>> getApplications(@Param("user") Integer user, @Param("deviceId") Integer deviceId, @Param("status") String status,@Param("type") String type, @Param("auditUser") Integer auditUser);
}
