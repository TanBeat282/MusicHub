package com.tandev.musichub.model.chart.chart_home;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemWeekChart implements Serializable {
    private String banner;
    private String playlistId;
    private int chartId;
    private String cover;
    private String country;
    private String type;
    private ArrayList<GroupItemWeekChart> group;
    private String link;
    private int week;
    private int year;
    private int latestWeek;
    private String startDate;
    private String endDate;
    private ArrayList<Items> items;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public int getChartId() {
        return chartId;
    }

    public void setChartId(int chartId) {
        this.chartId = chartId;
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

    public ArrayList<GroupItemWeekChart> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<GroupItemWeekChart> group) {
        this.group = group;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getLatestWeek() {
        return latestWeek;
    }

    public void setLatestWeek(int latestWeek) {
        this.latestWeek = latestWeek;
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

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }
}
