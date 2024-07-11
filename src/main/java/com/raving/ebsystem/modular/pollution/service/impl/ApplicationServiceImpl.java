package com.raving.ebsystem.modular.pollution.service.impl;

import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.exception.BizExceptionEnum;
import com.raving.ebsystem.common.exception.BussinessException;
import com.raving.ebsystem.common.persistence.dao.ApplicationMapper;
import com.raving.ebsystem.common.persistence.dao.DeviceMapper;
import com.raving.ebsystem.common.persistence.model.Application;
import com.raving.ebsystem.common.persistence.model.Device;
import com.raving.ebsystem.core.util.ToolUtil;
import com.raving.ebsystem.modular.pollution.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.raving.ebsystem.core.shiro.ShiroKit.getUser;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Override
    public void solveOperation(Application application) {
        String status = application.getStatus();
        application = applicationMapper.selectByPrimaryKey(application.getId());
        int deviceId = application.getDeviceId();
        if (!ConstantFactory.me().checkDeviceIsExist(application.getDeviceId())) {
            throw new BussinessException(BizExceptionEnum.DEVICE_NOT_EXIST);
        }
        String operationType = application.getType();
        Device device = deviceMapper.selectByPrimaryKey(deviceId);
        application.setStatus(status);
        if (ToolUtil.equals(status, "1") && ToolUtil.equals(operationType, "0")) {
            // 设备申请
            if (isDeviceOccupied(deviceId)) {
                throw new BussinessException(BizExceptionEnum.DEVICE_IS_OCCUPIED);
            }

            device.setDept(Objects.requireNonNull(getUser()).getDeptId());
            deviceMapper.updateByPrimaryKeySelective(device);
        } else if (ToolUtil.equals(status, "1") && ToolUtil.equals(operationType, "1")) {
            // 设备维护
            int repairCount = device.getRepairCount();
            device.setRepairCount(repairCount + 1);
            device.setRepairTime(System.currentTimeMillis());
            deviceMapper.updateByPrimaryKeySelective(device);
        } else if (ToolUtil.equals(status, "1") && ToolUtil.equals(operationType, "2")) {
           // 设备归还
            device.setDept(null);
            device.setStatus("0");
            deviceMapper.updateByPrimaryKey(device);
        }
        application.setAuditUser(Objects.requireNonNull(getUser()).getId());
        application.setAuditTime(System.currentTimeMillis());
        applicationMapper.updateByPrimaryKeySelective(application);
    }

    private boolean isDeviceOccupied(int deviceId) {
        Device device = deviceMapper.selectByPrimaryKey(deviceId);
        return ToolUtil.equals(device.getStatus(), "1");
    }
}
