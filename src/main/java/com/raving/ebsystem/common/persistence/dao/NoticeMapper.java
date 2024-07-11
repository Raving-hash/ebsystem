package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.Notice;
import com.raving.ebsystem.core.support.DateTime;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 通知表 Mapper 接口
 * </p>
 */
public interface NoticeMapper extends Mapper<Notice> {

    List<Map<String, Object>> list(@Param("condition") String condition);

    List<Map<String, Object>> selectAllBefore(@Param("time") DateTime dateTime);

    List<Map<String, Object>> selectAllByType(@Param("type") Integer type);

}