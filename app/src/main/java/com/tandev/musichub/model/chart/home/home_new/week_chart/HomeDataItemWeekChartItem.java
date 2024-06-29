package com.tandev.musichub.model.chart.home.home_new.week_chart;

import com.tandev.musichub.model.chart.chart_home.ItemWeekChart;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDataItemWeekChartItem implements Serializable {
    private String banner;
    private String cover;
    private String country;
    private String type;
    private ArrayList<HomeGroup> group;
    private String link;
    private String startDate;
    private String endDate;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<HomeGroup> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<HomeGroup> group) {
        this.group = group;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
