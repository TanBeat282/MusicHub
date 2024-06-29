package com.tandev.musichub.model.chart.home.home_new.new_release_chart;

import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;
import com.tandev.musichub.model.chart.home.home_new.new_release.HomeDataItemNewReleaseItem;
import com.tandev.musichub.model.chart.home.home_new.option.HomeOption;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDataItemNewReleaseChart implements HomeDataItem, Serializable {
    private static final long serialVersionUID = 1L;
    private String banner;
    private String type;
    private String link;
    private String title;
    private String sectionType;
    private String sectionId;
    private String viewType;
    private ArrayList<Items> items;
    private HomeOption options;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public HomeOption getOptions() {
        return options;
    }

    public void setOptions(HomeOption options) {
        this.options = options;
    }
}
