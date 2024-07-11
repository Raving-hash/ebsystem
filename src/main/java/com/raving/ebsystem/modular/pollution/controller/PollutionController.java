package com.raving.ebsystem.modular.pollution.controller;

import com.raving.ebsystem.common.annotion.Permission;
import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.controller.BaseController;
import com.raving.ebsystem.common.exception.BizExceptionEnum;
import com.raving.ebsystem.common.exception.BussinessException;
import com.raving.ebsystem.common.persistence.dao.PollutionMapper;
import com.raving.ebsystem.common.persistence.model.Pollution;
import com.raving.ebsystem.core.util.ToolUtil;
import com.raving.ebsystem.modular.pollution.service.PollutionService;
import com.raving.ebsystem.modular.pollution.wrapper.PollutionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pollution")
public class PollutionController extends BaseController {
    @Autowired
    private PollutionMapper pollutionMapper;

    @Autowired
    private PollutionService pollutionService;

    private String PREFIX = "/system/pollution/";

    /**
     * 跳转到污染物管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pollution.html";
    }

    /**
     * 添加污染
     */
    @RequestMapping("/add")
    @Permission
    @ResponseBody
    public Object pollutionAdd(Pollution pollution) {
        if (ToolUtil.isOneEmpty(pollution, pollution.getValue())) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        if (!ConstantFactory.me().checkPollutionTypeIsExist(pollution.getPollutiontype())) {
            throw new BussinessException(BizExceptionEnum.POLLUTION_TYPE_NOT_EXIST);
        }
        if (!ConstantFactory.me().checkWaterSourceIsExist(pollution.getWatersource())) {
            throw new BussinessException(BizExceptionEnum.WATER_SOURCE_NOT_EXIST);
        }

        long currTime = System.currentTimeMillis();
        pollution.setUpdatetime(currTime);
        return pollutionMapper.insert(pollution);
    }

    public void addRandomPollution() {
        //生成100条不同的pollution.并插入到数据库中
        for (int i = 0; i < 1000; i++) {
            Pollution pollution = new Pollution();
            pollution.setPollutiontype((int) ((Math.random() * 100) % 13 + 13));
            pollution.setWatertype(String.valueOf((int) (Math.random() * 10) % 2));
            pollution.setWatersource((int) (Math.random() * 10) % 6 + 1);
            pollution.setValue((int) (Math.random() * 100));
            pollution.setUpdatetime(System.currentTimeMillis());
            pollutionMapper.insert(pollution);
        }
    }

    /**
     * 删除污染
     */
    @RequestMapping("/delete")
    @Permission
    @ResponseBody
    public Object pollutionDelete(Integer id) {
        pollutionMapper.deleteByPrimaryKey(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改污染
     */
    @RequestMapping("/update")
    @Permission
    @ResponseBody
    public Object pollutionUpdate(Pollution pollution) {
        if (ToolUtil.isOneEmpty(pollution)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        long currTime = System.currentTimeMillis();
        pollution.setUpdatetime(currTime);
        pollutionMapper.updateByPrimaryKey(pollution);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/selectPollutionsInDays")
    @Permission
    @ResponseBody
    public Object selectPollutionsInDays(@RequestParam(name = "pollutionTypeId", required = false) Integer pollutionTypeId,
                                         @RequestParam(name = "waterSourceId", required = false) Integer waterSourceId,
                                         @RequestParam(defaultValue = "7") Integer days) {
        long timeStamp = System.currentTimeMillis();
        // 将天数转换为毫秒数
        long daysInMilliSeconds = (long) days * 24 * 60 * 60 * 1000;
        // 将毫秒数转换为时间戳
        timeStamp -= daysInMilliSeconds;
        List<Map<String, Object>> pollutions = pollutionService.selectPollutionsInDays(pollutionTypeId, waterSourceId, timeStamp);
        return super.warpObject(new PollutionWrapper(pollutions));
    }
}
