package com.raving.ebsystem.core.job;

import com.raving.ebsystem.common.persistence.dao.PollutionMapper;
import com.raving.ebsystem.common.persistence.dao.PollutionTypeMapper;
import com.raving.ebsystem.common.persistence.model.Pollution;
import com.raving.ebsystem.common.persistence.model.PollutionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class AutoMakePollution {
    @Autowired
    private PollutionMapper pollutionMapper;
    @Autowired
    private PollutionTypeMapper pollutionTypeMapper;

    private static final int INTERVAL_TIME = 1000 * 60 * 30;
    Integer[] pollutionTypeArray = {13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    Integer[] waterSourceArray = {7, 8, 9, 10, 11, 12, 13, 14, 15};

    // 60s 生成一条污染数据
    @Scheduled(fixedRate = INTERVAL_TIME, initialDelay = 5000)
    public void makePollutionJob() {
        for (int i = 0; i < 10; i++) {
            makePollution();
        }
    }

    private void makePollution() {
        Pollution pollution = new Pollution();
        pollution.setId(null);
        pollution.setWatertype(String.valueOf((int) (Math.random() * 10 % 2)));
        pollution.setPollutiontype(getRandomElement(pollutionTypeArray));
        pollution.setUpdatetime(System.currentTimeMillis());
        //pollution.setUpdatetime(getRandomDateTime());
        pollution.setWatersource(getRandomElement(waterSourceArray));
        pollution.setValue(getRandomLimit(pollution.getPollutiontype()));
        pollutionMapper.insert(pollution);
    }

    /**
     * 从整形数组里面随机获取一个元素
     */
    private Integer getRandomElement(Integer[] array) {
        return array[(int) (Math.random() * array.length)];
    }

    /**
     * 返回一个ms时间戳， 时间是当前时间前后30天的随机时间
     */
    private Long getRandomDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, (int) (Math.random() * 60 - 30));
        return calendar.getTimeInMillis();
    }

    /**
     * 根据污染类型查询阈值，然后生成阈值相关的随机数
     */
    private double getRandomLimit(Integer pollutionType) {
        PollutionType pollutionType1 = pollutionTypeMapper.selectByPrimaryKey(pollutionType);
        double limit = pollutionType1.getLimitvalue();
        // 生成超标概率 范围：0% - 20%
        double overRandom = Math.random() * 20;
       // 生成合规概率， 范围： 0% - 80%
        double normalRandom = Math.random() * 80;
        // 生成超标值
        double overValue = limit + limit * overRandom / 100;
        // 生成合规值
        double normalValue = limit - limit * normalRandom / 100;
        // 生成随机值
        double randomValue = Math.random() * (overValue - normalValue) + normalValue;
        return randomValue;
    }
}
