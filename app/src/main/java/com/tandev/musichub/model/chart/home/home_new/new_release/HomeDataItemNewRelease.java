package com.tandev.musichub.model.chart.home.home_new.new_release;

import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;

import java.io.Serializable;

public class HomeDataItemNewRelease implements HomeDataItem, Serializable {
    private static final long serialVersionUID = 1L;
    private String sectionType;
    private String title;
    private String link;
    private HomeDataItemNewReleaseItem items;

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
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

    public HomeDataItemNewReleaseItem getItems() {
        return items;
    }

    public void setItems(HomeDataItemNewReleaseItem items) {
        this.items = items;
    }
}
