package com.tandev.musichub.model.chart.home.home_new.rt_chart;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeChart implements Serializable {
    private ArrayList<HomeChartTime> times;

    public ArrayList<HomeChartTime> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<HomeChartTime> times) {
        this.times = times;
    }
}
