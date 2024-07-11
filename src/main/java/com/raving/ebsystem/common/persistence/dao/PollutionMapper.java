package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.Pollution;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 污染物
 */
public interface PollutionMapper extends Mapper<Pollution> {

    List<Map<String, Object>> selectPollutionsInDays(@Param("pollutionTypeId") Integer pollutionTypeId,
                                                     @Param("waterSourceId") Integer waterSourceId,
                                                     @Param("timeStamp") long timeStamp);

}
