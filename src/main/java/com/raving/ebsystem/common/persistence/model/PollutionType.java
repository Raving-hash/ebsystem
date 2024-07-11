package com.raving.ebsystem.common.persistence.model;

import javax.persistence.Table;

/**
 * 污染物类型
 */
@Table(name = "t_pollution_type")
public class PollutionType extends Base{
    private String name;
    private String unit;
    private double limitvalue;

    public double getLimitvalue() {
        return limitvalue;
    }

    public void setLimitvalue(double limitvalue) {
        this.limitvalue = limitvalue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PollutionType{" +
                "id=" + id +
                ", name=" + name +
                "}";
    }
}
