package com.raving.ebsystem.common.persistence.model;

import com.raving.ebsystem.core.support.DateTime;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Table(name = "t_warn_water")
@Getter
@Setter
public class WarnWater extends Base {
    private Integer waterSourceId;
    private DateTime createTime;
}
