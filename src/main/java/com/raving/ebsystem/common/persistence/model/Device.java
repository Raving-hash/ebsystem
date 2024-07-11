package com.raving.ebsystem.common.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Table(name = "t_device")
@Getter
@Setter
public class Device extends Base{
    private String name;
    private Integer dept;
    private Long createTime;
    private Long repairTime;
    private int repairCount;
    private String status;  // 0 - 未分配， 1 - 已分配
}
