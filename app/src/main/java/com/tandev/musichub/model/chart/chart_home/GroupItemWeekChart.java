package com.tandev.musichub.model.chart.chart_home;

import java.io.Serializable;

public class GroupItemWeekChart implements Serializable {
    private int id;
    private String name;
    private String type;
    private String link;

    public GroupItemWeekChart() {
    }

    public GroupItemWeekChart(int id, String name, String type, String link) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
