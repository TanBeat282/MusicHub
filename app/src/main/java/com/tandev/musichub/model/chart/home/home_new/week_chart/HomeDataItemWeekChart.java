package com.tandev.musichub.model.chart.home.home_new.week_chart;

import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;
import com.tandev.musichub.model.chart.home.home_new.rt_chart.HomeChart;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDataItemWeekChart implements HomeDataItem, Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<HomeDataItemWeekChartItem> items;
    private String sectionType;

    public ArrayList<HomeDataItemWeekChartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<HomeDataItemWeekChartItem> items) {
        this.items = items;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }
}
