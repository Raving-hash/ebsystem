package com.raving.ebsystem.common.persistence.model;

import javax.persistence.Table;

/**
 * 污染物
 */
@Table(name = "t_pollution")
public class Pollution extends Base{
    private int pollutiontype;   // 污染物类型
    private double value;
    private String watertype;  // 水源类型，0-工业用水，1-生活用水
    private int watersource;
    private long updatetime;
    public int getWatersource() {
        return watersource;
    }

    public void setWatersource(int watersource) {
        this.watersource = watersource;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public int getPollutiontype() {
        return pollutiontype;
    }

    public void setPollutiontype(int pollutiontype) {
        this.pollutiontype = pollutiontype;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getWatertype() {
        return watertype;
    }

    public void setWatertype(String watertype) {
        this.watertype = watertype;
    }

    @Override
    public String toString() {
        return "Pollution{" +
                "id=" + id +
                ", pollutiontype=" + pollutiontype +
                ", value=" + value +
                ", watertype=" + watertype +
                ", waterSource=" + watersource +
                ", updateTime=" + updatetime +
                "}";
    }
}
