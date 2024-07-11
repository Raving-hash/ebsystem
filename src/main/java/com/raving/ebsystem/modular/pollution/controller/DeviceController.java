package com.raving.ebsystem.modular.pollution.controller;

import com.raving.ebsystem.common.annotion.Permission;
import com.raving.ebsystem.common.controller.BaseController;
import com.raving.ebsystem.common.exception.BizExceptionEnum;
import com.raving.ebsystem.common.exception.BussinessException;
import com.raving.ebsystem.common.persistence.dao.DeviceMapper;
import com.raving.ebsystem.common.persistence.model.Device;
import com.raving.ebsystem.core.log.LogObjectHolder;
import com.raving.ebsystem.core.util.ToolUtil;
import com.raving.ebsystem.modular.pollution.wrapper.DeviceWrapper;
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
@RequestMapping("/device")
public class DeviceController extends BaseController {
    @Autowired
    private DeviceMapper deviceMapper;
    private String PREFIX = "/system/device/";

    @RequestMapping("")
    public String index() {
        return PREFIX + "device.html";
    }

    @RequestMapping("/getAdd")
    public String getAdd() {
        return PREFIX + "device_add.html";
    }

    @RequestMapping("/getEdit/{deviceId}")
    public String getEdit(@PathVariable(name = "deviceId") Integer deviceId, Model model) {
        if (ToolUtil.isEmpty(deviceId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Device device = deviceMapper.selectByPrimaryKey(deviceId);
        model.addAttribute(device);
        LogObjectHolder.me().set(device);
        return PREFIX + "device_edit.html";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = deviceMapper.list(condition);
        return super.warpObject(new DeviceWrapper(list));
    }

    @RequestMapping("/add")
    @Permission
    @ResponseBody
    public Object add(Device device){
        if (ToolUtil.isEmpty(device)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        device.setId(null);
        device.setCreateTime(System.currentTimeMillis());
        device.setStatus("0");
        deviceMapper.insert(device);
        return SUCCESS_TIP;
    }

    @RequestMapping("/delete")
    @Permission
    @ResponseBody
    public Object delete(Integer deviceId) {
        if (ToolUtil.isEmpty(deviceId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        deviceMapper.deleteByPrimaryKey(deviceId);
        return SUCCESS_TIP;
    }

    @RequestMapping("/edit")
    @Permission
    @ResponseBody
    public Object edit(Device device) {
        if (ToolUtil.isEmpty(device)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        deviceMapper.updateByPrimaryKeySelective(device);
        return SUCCESS_TIP;
    }

    @RequestMapping("/setStatus")
    @Permission
    @ResponseBody
    public Object setStatus(@RequestParam("id") Integer deviceId, String status) {
        if (ToolUtil.isOneEmpty(status, deviceId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        deviceMapper.setStatus(deviceId, status);
        return SUCCESS_TIP;
    }

    @RequestMapping("/setDept")
    @Permission
    @ResponseBody
    public Object setDept(@RequestParam("id") Integer deviceId, Integer deptId) {
        if (ToolUtil.isOneEmpty(deptId, deviceId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        deviceMapper.setDept(deviceId, deptId);
        return SUCCESS_TIP;
    }

    @RequestMapping("/getDevicesByStatus")
    @ResponseBody
    public Object getDevicesByStatus(String status) {
        if (ToolUtil.isOneEmpty(status)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        List<Map<String, Object>> devices = deviceMapper.getDevicesByStatus(status);
        return super.warpObject(new DeviceWrapper(devices));
    }

}
