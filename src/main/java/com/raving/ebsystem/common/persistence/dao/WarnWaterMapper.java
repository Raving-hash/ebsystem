package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.WarnWater;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WarnWaterMapper extends Mapper<WarnWater> {
    Integer selectCountByWaterSourceIdBefore(@Param("waterSourceId") Integer waterSourceId, @Param("dateTime") String dateTime);

    List<Map<String, Object>> selectWarnWaterSourceRank(@Param("dateTime") String dateTime);
}
