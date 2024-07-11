package com.raving.ebsystem.common.persistence.model;

import javax.persistence.Table;

@Table(name = "t_water_source")
public class WaterSource extends Base {
    private String name;
    private String phone;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
