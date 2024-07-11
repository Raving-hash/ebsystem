package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.PollutionType;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 污染物类型
 */
public interface PollutionTypeMapper extends BaseMapper<PollutionType> {
    /**
     * 查询污染类型列表
     *
     */
    List<Map<String, Object>> list(@Param("condition") String condition);
    void insertUseGeneratedKeys(@Param("pollutionType") PollutionType pollutionType);

    PollutionType getPollutionTypeByName(@Param("pollutionTypeName") String pollutionTypeName);

    PollutionType getPollutionTypeById(@Param("pollutionTypeId") Integer pollutionTypeId);


}
