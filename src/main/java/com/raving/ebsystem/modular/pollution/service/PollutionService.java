package com.raving.ebsystem.modular.pollution.service;

import java.util.List;
import java.util.Map;

public interface PollutionService {
    List<Map<String, Object>> selectPollutionsInDays(Integer pollutionTypeId, Integer waterSourceId, Long timeStamp);
}
