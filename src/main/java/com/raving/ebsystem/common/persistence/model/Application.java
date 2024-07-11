package com.raving.ebsystem.common.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Table(name = "t_application")
@Getter
@Setter
public class Application extends Base{
    private Integer deviceId;
    private Long applyTime;
    // 0 - 设备申请， 1 - 设备维护， 2 - 设备归还
    private String type;
    private String status;
    private Integer user;
    private Integer auditUser;
    private Long editTime;
    private Long auditTime;

}
