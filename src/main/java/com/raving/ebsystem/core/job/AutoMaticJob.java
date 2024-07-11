package com.raving.ebsystem.core.job;

import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.persistence.dao.*;
import com.raving.ebsystem.common.persistence.model.Notice;
import com.raving.ebsystem.common.persistence.model.WarnWater;
import com.raving.ebsystem.common.persistence.model.WaterSource;
import com.raving.ebsystem.core.support.DateTime;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AutoMaticJob {
    @Autowired
    private PollutionMapper pollutionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PollutionTypeMapper pollutionTypeMapper;
    @Autowired
    private WaterSourceMapper waterSourceMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private WarnWaterMapper warnWaterMapper;

    private final Integer LIMIT_COUNT = 2;

    private static final int INTERVAL_TIME = 1000 * 60 * 20;

    @Scheduled(fixedRate = INTERVAL_TIME, initialDelay = 5 * 60 * 1000)
    public void checkAllWaterSource() {
        Integer aobingId = userMapper.getByAccount("aobing").getId();
        Map<WaterSource, List<String>> warnList = getWarnList();
        Notice notice = new Notice();
        notice.setTitle("警告");
        notice.setId(null);
        notice.setCreater(aobingId);
        notice.setType(10);

        warnList.forEach((ws, pollutionTypeList) -> {
            notice.setCreatetime(new Date());
            notice.setContent(String.format("发现水源：%s 出现异常污染：", ws.getName()) + StringUtils.join(pollutionTypeList, '；') + " 。 时间：" + new Date());
            noticeMapper.insert(notice);
            notice.setId(null);

            WarnWater warnWater = new WarnWater();
            warnWater.setId(null);
            warnWater.setWaterSourceId(ws.getId());
            warnWater.setCreateTime(new DateTime(System.currentTimeMillis()));
            warnWaterMapper.insert(warnWater);
        });
    }
    private Map<WaterSource, List<String>> getWarnList() {
        Map<WaterSource, List<String>> warnList = new HashMap<>();
        List<WaterSource> waterSources = waterSourceMapper.selectAll();
        waterSources.forEach(ws -> {
            int wsId = ws.getId();
            long currTime = System.currentTimeMillis();
            currTime = currTime - INTERVAL_TIME;
            List<Map<String, Object>> pollutions = pollutionMapper.selectPollutionsInDays(null, wsId, currTime);
            Map<Integer, Integer> pollutionCountMap = new HashMap<>();
            pollutions.forEach(entry -> {
                int pollutionTypeId = (Integer) entry.get("pollutiontype");
                double value = (double) entry.get("value");
                if (ConstantFactory.me().isOverStandard(value, pollutionTypeId)) {
                    int count = pollutionCountMap.getOrDefault(pollutionTypeId, 0);
                    pollutionCountMap.put(pollutionTypeId, count + 1);
                }
            });
            pollutionCountMap.forEach((pollutionTypeId, warnCount) -> {
                if (warnCount > LIMIT_COUNT) {
                    List<String> countList = warnList.getOrDefault(ws, new ArrayList<>());
                    countList.add(pollutionTypeMapper.selectByPrimaryKey(pollutionTypeId).getName());
                    warnList.put(ws, countList);
                }
            });
        });
        return warnList;
    }

}
