package com.raving.ebsystem.common.constant.tips;

import com.raving.ebsystem.common.exception.BizExceptionEnum;

/**
 * 返回给前台的错误提示
 */
public class ErrorTip extends Tip {

    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ErrorTip(BizExceptionEnum bizExceptionEnum) {
        this.code = bizExceptionEnum.getCode();
        this.message = bizExceptionEnum.getMessage();
    }
}
