package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.Dict;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 字典表 Mapper 接口
 * </p>
 *
 */
public interface DictMapper extends Mapper<Dict> {

    /**
     * 根据编码获取词典列表
     *
     * @param code
     * @return
     */
    List<Dict> selectByCode(@Param("code") String code);

    /**
     * 查询字典列表
     *
     */
    List<Map<String, Object>> list(@Param("condition") String conditiion);
}