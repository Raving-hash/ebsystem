package com.raving.ebsystem.core.log.factory;

import com.raving.ebsystem.common.constant.state.LogSucceed;
import com.raving.ebsystem.common.constant.state.LogType;
import com.raving.ebsystem.common.persistence.model.LoginLog;
import com.raving.ebsystem.common.persistence.model.OperationLog;

import java.util.Date;

/**
 * 日志对象创建工厂
 */
public class LogFactory {
    static long offset = 8 * 60 * 60 * 1000;

    /**
     * 创建操作日志
     */
    public static OperationLog createOperationLog(LogType logType, Integer userId, String bussinessName, String clazzName, String methodName, String msg, LogSucceed succeed) {
        OperationLog operationLog = new OperationLog();
        operationLog.setLogtype(logType.getMessage());
        operationLog.setLogname(bussinessName);
        operationLog.setUserid(userId);
        operationLog.setClassname(clazzName);
        operationLog.setMethod(methodName);
        operationLog.setCreatetime(new Date(System.currentTimeMillis() + offset));
        operationLog.setSucceed(succeed.getMessage());
        operationLog.setMessage(msg);
        return operationLog;
    }

    /**
     * 创建登录日志
     */
    public static LoginLog createLoginLog(LogType logType, Integer userId, String msg, String ip) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLogname(logType.getMessage());
        loginLog.setUserid(userId);
        // 获取UTC到上海的ms时差
        loginLog.setCreatetime(new Date(System.currentTimeMillis() + offset));
        loginLog.setSucceed(LogSucceed.SUCCESS.getMessage());
        loginLog.setIp(ip);
        loginLog.setMessage(msg);
        return loginLog;
    }
}
