package com.raving.ebsystem.modular.pollution.controller;

import com.raving.ebsystem.common.annotion.Permission;
import com.raving.ebsystem.common.controller.BaseController;
import com.raving.ebsystem.common.exception.BizExceptionEnum;
import com.raving.ebsystem.common.exception.BussinessException;
import com.raving.ebsystem.common.persistence.dao.PollutionTypeMapper;
import com.raving.ebsystem.common.persistence.model.PollutionType;
import com.raving.ebsystem.core.log.LogObjectHolder;
import com.raving.ebsystem.core.util.ToolUtil;
import com.raving.ebsystem.modular.pollution.wrapper.PollutionTypeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pollution/type")
public class PollutionTypeController extends BaseController {

    @Autowired
    private PollutionTypeMapper pollutionTypeMapper;
    private String PREFIX = "/system/pollution_type";

    @RequestMapping(value = "/getAdd")
    public String getAdd() {
        return PREFIX + "/pollution_type_add.html";
    }

    @RequestMapping("")
    public String index() {
        return PREFIX + "/pollution_type.html";
    }

    @RequestMapping(value = "/add")
    @Permission
    @ResponseBody
    public Object add(PollutionType pollutionType) {
        if (ToolUtil.isOneEmpty(pollutionType)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        pollutionTypeMapper.insertUseGeneratedKeys(pollutionType);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public Object delete(@RequestParam int pollutionTypeId) {
        pollutionTypeMapper.deleteByPrimaryKey(pollutionTypeId);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String condition) {
        List<Map<String, Object>> list = pollutionTypeMapper.list(condition);
        return super.warpObject(new PollutionTypeWrapper(list));
    }

    /**
     * 修改污染类型信息
     */
    @RequestMapping(value = "/update")
    @Permission
    @ResponseBody
    public Object update(PollutionType pollutionType) {
        if (ToolUtil.isOneEmpty(pollutionType)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        pollutionTypeMapper.updateByPrimaryKey(pollutionType);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到修改污染类型信息页面
     */
    @RequestMapping(value = "/getUpdate/{pollutionTypeId}")
    public String getUpdate(@PathVariable("pollutionTypeId") Integer pollutionTypeId, Model model) {
        if (ToolUtil.isEmpty(pollutionTypeId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        PollutionType pollutionType = pollutionTypeMapper.getPollutionTypeById(pollutionTypeId);
        model.addAttribute(pollutionType);
        LogObjectHolder.me().set(pollutionType);
        return PREFIX + "/pollution_type_edit.html";
    }
}
