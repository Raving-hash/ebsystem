package com.raving.ebsystem.modular.pollution.controller;

import com.raving.ebsystem.common.annotion.Permission;
import com.raving.ebsystem.common.annotion.log.BussinessLog;
import com.raving.ebsystem.common.constant.Dict;
import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.controller.BaseController;
import com.raving.ebsystem.common.exception.BizExceptionEnum;
import com.raving.ebsystem.common.exception.BussinessException;
import com.raving.ebsystem.common.persistence.dao.ApplicationMapper;
import com.raving.ebsystem.common.persistence.dao.DeviceMapper;
import com.raving.ebsystem.common.persistence.model.Application;
import com.raving.ebsystem.common.persistence.model.Device;
import com.raving.ebsystem.core.log.LogObjectHolder;
import com.raving.ebsystem.core.shiro.ShiroKit;
import com.raving.ebsystem.core.util.ToolUtil;
import com.raving.ebsystem.modular.pollution.service.ApplicationService;
import com.raving.ebsystem.modular.pollution.wrapper.ApplicationWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.raving.ebsystem.core.shiro.ShiroKit.getUser;

@Controller
@RequestMapping("/application")
public class ApplicationController extends BaseController {
    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ApplicationService applicationService;

    private String PREFIX = "/system/application/";

    @RequestMapping("")
    public String index() {
        return PREFIX + "application_apply.html";
    }

    @RequestMapping("/getAdd")
    public String getAdd() {
        return PREFIX + "application_add.html";
    }

    @RequestMapping("/getEdit/{applicationId}")
    public String getEdit(@PathVariable(name = "applicationId") Integer applicationId, Model model) {
        if (ToolUtil.isEmpty(applicationId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Application application = applicationMapper.selectByPrimaryKey(applicationId);
        model.addAttribute(application);
        LogObjectHolder.me().set(application);
        return PREFIX + "application_edit.html";
    }

    @RequestMapping("/getApplicationsByStatus")
    @ResponseBody
    public Object getApplicationsByStatus(String status) {
        if (ToolUtil.isEmpty(status)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        List<Map<String, Object>> applicationsByStatus = applicationMapper.getApplicationsByStatus(status);
        return super.warpObject(new ApplicationWarpper(applicationsByStatus));
    }

    @RequestMapping("/getApplicationsByDept")
    @ResponseBody
    public Object getApplicationsByDept(Integer deviceId) {
        if (ToolUtil.isEmpty(deviceId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        List<Map<String, Object>> applicationsByDept = applicationMapper.getApplicationsByDept(deviceId);
        return super.warpObject(new ApplicationWarpper(applicationsByDept));
    }

    @RequestMapping("/getApplicationsByUser")
    @ResponseBody
    public Object getApplicationsByUser(@RequestParam(required = false) Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            userId = Objects.requireNonNull(getUser()).getId();
        }
        List<Map<String, Object>> applicationsByUser = applicationMapper.getApplicationsByUser(userId);
        return super.warpObject(new ApplicationWarpper(applicationsByUser));
    }

    @RequestMapping("/add")
    @Permission
    @ResponseBody
    public Object add(Application application) {
        if (ToolUtil.isEmpty(application)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        if (!ConstantFactory.me().checkDeviceIsExist(application.getDeviceId())) {
            throw new BussinessException(BizExceptionEnum.DEVICE_NOT_EXIST);
        }
        if (!Objects.equals(application.getType(), "0")) {
            Integer deviceId = application.getDeviceId();
            Device device = deviceMapper.selectByPrimaryKey(deviceId);
            if (!Objects.equals(device.getStatus(), "1") || !Objects.equals(device.getDept(), Objects.requireNonNull(getUser()).getDeptId())) {
                throw new BussinessException(BizExceptionEnum.NO_PERMITION);
            }
        }
        application.setId(null);
        application.setStatus("0");
        application.setUser(Objects.requireNonNull(getUser()).getId());
        application.setApplyTime(System.currentTimeMillis());
        application.setEditTime(System.currentTimeMillis());
        applicationMapper.insert(application);
        return SUCCESS_TIP;
    }

    @RequestMapping("/edit")
    @Permission
    @ResponseBody
    public Object edit(Application application) {
        if (ToolUtil.isEmpty(application)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        if (!ConstantFactory.me().checkDeviceIsExist(application.getDeviceId())) {
            throw new BussinessException(BizExceptionEnum.DEVICE_NOT_EXIST);
        }
        if (!checkUserCorrect(application.getId())) {
            throw new BussinessException(BizExceptionEnum.NO_PERMITION);
        }
        application.setEditTime(System.currentTimeMillis());
        applicationMapper.updateByPrimaryKeySelective(application);
        return SUCCESS_TIP;
    }

    @RequestMapping("/delete")
    @Permission
    @ResponseBody
    @BussinessLog(value = "删除申请", key = "applicationId", dict = Dict.ApplicationDict)
    public Object delete(@RequestParam Integer applicationId) {
        if (ToolUtil.isEmpty(applicationId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        if (!checkUserCorrect(applicationId)) {
            throw new BussinessException(BizExceptionEnum.NO_PERMITION);
        }
        applicationMapper.deleteByPrimaryKey(applicationId);
        return SUCCESS_TIP;
    }

    private boolean checkUserCorrect(Integer applicationId) {
        Application application = applicationMapper.selectByPrimaryKey(applicationId);
        return Objects.equals(application.getUser(), Objects.requireNonNull(getUser()).getId()) || ShiroKit.isAdmin();
    }

    @RequestMapping("/getAudit")
    public String getAudit() {
        return PREFIX + "application_audit.html";
    }

    @RequestMapping("/audit")
    @Permission
    @ResponseBody
    @BussinessLog(value = "审核申请", key = "applicationId", dict = Dict.ApplicationDict)
    public Object audit(Application application) {
        if (ToolUtil.isOneEmpty(application, application.getId())) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        String status = applicationMapper.selectByPrimaryKey(application.getId()).getStatus();
        if (!ToolUtil.equals(status, "0")) {
            throw new BussinessException(BizExceptionEnum.APPLICATION_ALREADY_AUDIT);
        }
        applicationService.solveOperation(application);
        return SUCCESS_TIP;
    }

    @RequestMapping("/search")
    @ResponseBody
    public Object search(Application application) {
        if (ToolUtil.isOneEmpty(application)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        List<Map<String, Object>> applications = applicationMapper.
                getApplications(application.getUser(),
                application.getDeviceId(),
                application.getStatus(),
                application.getType(),
                application.getAuditUser());
        return super.warpObject(new ApplicationWarpper(applications));
    }



}
