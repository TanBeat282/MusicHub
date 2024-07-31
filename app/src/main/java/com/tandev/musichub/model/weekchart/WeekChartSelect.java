package com.tandev.musichub.model.weekchart;

import java.io.Serializable;

public class WeekChartSelect implements Serializable {
    private int week;
    private String startDayWeek;
    private String endDayWeek;

    public WeekChartSelect(int week, String startDayWeek, String endDayWeek) {
        this.week = week;
        this.startDayWeek = startDayWeek;
        this.endDayWeek = endDayWeek;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getStartDayWeek() {
        return startDayWeek;
    }

    public void setStartDayWeek(String startDayWeek) {
        this.startDayWeek = startDayWeek;
    }

    public String getEndDayWeek() {
        return endDayWeek;
    }

    public void setEndDayWeek(String endDayWeek) {
        this.endDayWeek = endDayWeek;
    }
}
