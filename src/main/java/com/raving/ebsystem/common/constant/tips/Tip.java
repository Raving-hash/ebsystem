package com.raving.ebsystem.common.constant.tips;

/**
 * 返回给前台的提示（最终转化为json形式）
 */
public abstract class Tip {

    protected int code;
    protected String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
