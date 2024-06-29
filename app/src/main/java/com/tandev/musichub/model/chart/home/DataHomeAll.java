package com.tandev.musichub.model.chart.home;

import com.tandev.musichub.model.chart.home.home_new.new_release.HomeDataItemNewReleaseItem;

import java.io.Serializable;

public class DataHomeAll implements Serializable {
    private String sectionType;
    private String viewType;
    private String title;
    private String link;
    private String sectionId;
    private HomeDataItemNewReleaseItem items;

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public HomeDataItemNewReleaseItem getItems() {
        return items;
    }

    public void setItems(HomeDataItemNewReleaseItem items) {
        this.items = items;
    }
}
