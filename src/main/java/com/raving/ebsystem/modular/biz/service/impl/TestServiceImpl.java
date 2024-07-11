package com.raving.ebsystem.modular.biz.service.impl;

import com.raving.ebsystem.modular.biz.service.ITestService;
import com.raving.ebsystem.common.annotion.DataSource;
import com.raving.ebsystem.common.constant.DSEnum;
import com.raving.ebsystem.common.persistence.dao.TestMapper;
import com.raving.ebsystem.common.persistence.model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试服务
 */
@Service
public class TestServiceImpl implements ITestService {


    @Autowired
    TestMapper testMapper;

    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_BIZ)
    public void testBiz() {
        Test test = testMapper.selectByPrimaryKey(1);
        test.setId(22);
        testMapper.insert(test);
    }


    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_Ebsystem)
    public void testraving() {
        Test test = testMapper.selectByPrimaryKey(1);
        test.setId(33);
        testMapper.insert(test);
    }

    @Override
    @Transactional
    public void testAll() {
        testBiz();
        testraving();
        //int i = 1 / 0;
    }

}
