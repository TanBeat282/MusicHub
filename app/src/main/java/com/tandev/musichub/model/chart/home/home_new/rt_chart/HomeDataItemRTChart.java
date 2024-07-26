package com.tandev.musichub.model.chart.home.home_new.rt_chart;

import com.google.gson.JsonObject;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;
import com.tandev.musichub.model.chart.home.home_new.new_release.HomeDataItemNewReleaseItem;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDataItemRTChart implements HomeDataItem, Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Items> promotes;
    private ArrayList<Items> items;
    private JsonObject chart;
    private String chartType;
    private String sectionType;
    private String sectionId;

    public ArrayList<Items> getPromotes() {
        return promotes;
    }

    public void setPromotes(ArrayList<Items> promotes) {
        this.promotes = promotes;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public JsonObject getChart() {
        return chart;
    }

    public void setChart(JsonObject chart) {
        this.chart = chart;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
