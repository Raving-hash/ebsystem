package com.raving.ebsystem.modular.pollution.service.impl;

import com.raving.ebsystem.common.persistence.dao.WaterSourceMapper;
import com.raving.ebsystem.common.persistence.model.WaterSource;
import com.raving.ebsystem.modular.pollution.service.WaterSourceService;
import org.springframework.beans.factory.annotation.Autowired;

public class WaterSourceServiceImpl implements WaterSourceService {
    @Autowired
    private WaterSourceMapper waterSourceMapper;


    @Override
    public WaterSource getWaterSource(int id) {
        return waterSourceMapper.selectByPrimaryKey(id);
    }
}
