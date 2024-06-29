package com.tandev.musichub.model.chart.chart_home;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private RTCharts RTChart;
    private ArrayList<Items> newRelease;
    private WeekChart_Home weekChart;

    public RTCharts getRTChart() {
        return RTChart;
    }

    public void setRTChart(RTCharts RTChart) {
        this.RTChart = RTChart;
    }

    public ArrayList<Items> getNewRelease() {
        return newRelease;
    }

    public void setNewRelease(ArrayList<Items> newRelease) {
        this.newRelease = newRelease;
    }

    public WeekChart_Home getWeekChart() {
        return weekChart;
    }

    public void setWeekChart(WeekChart_Home weekChart) {
        this.weekChart = weekChart;
    }
}

