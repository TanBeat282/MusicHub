package com.tandev.musichub.model.chart.chart_home;

import java.io.Serializable;

public class WeekChart_Home implements Serializable {
    private ItemWeekChart vn;
    private ItemWeekChart us;
    private ItemWeekChart korea;

    public ItemWeekChart getVn() {
        return vn;
    }

    public void setVn(ItemWeekChart vn) {
        this.vn = vn;
    }

    public ItemWeekChart getUs() {
        return us;
    }

    public void setUs(ItemWeekChart us) {
        this.us = us;
    }

    public ItemWeekChart getKorea() {
        return korea;
    }

    public void setKorea(ItemWeekChart korea) {
        this.korea = korea;
    }
}
