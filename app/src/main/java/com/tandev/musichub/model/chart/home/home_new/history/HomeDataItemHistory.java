package com.tandev.musichub.model.chart.home.home_new.history;

import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;

import java.io.Serializable;

public class HomeDataItemHistory implements HomeDataItem, Serializable {
    private static final long serialVersionUID = 1L;
    private String sectionId;
    private String title;
    private String sectionType;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }
}
