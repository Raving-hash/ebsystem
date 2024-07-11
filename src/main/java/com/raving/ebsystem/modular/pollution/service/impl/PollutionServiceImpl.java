package com.raving.ebsystem.modular.pollution.service.impl;

import com.raving.ebsystem.common.persistence.dao.PollutionMapper;
import com.raving.ebsystem.modular.pollution.service.PollutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PollutionServiceImpl implements PollutionService {
    @Autowired
    private PollutionMapper pollutionMapper;

    @Override
    public List<Map<String, Object>> selectPollutionsInDays(Integer pollutionTypeId, Integer waterSourceId, Long timeStamp) {
        return pollutionMapper.selectPollutionsInDays(pollutionTypeId, waterSourceId, timeStamp);
    }
}
