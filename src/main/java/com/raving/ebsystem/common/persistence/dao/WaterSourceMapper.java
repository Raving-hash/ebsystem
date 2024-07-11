package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.WaterSource;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface WaterSourceMapper extends Mapper<WaterSource> {
    List<Map<String, Object>> list(@Param("condition") String condition);
}
