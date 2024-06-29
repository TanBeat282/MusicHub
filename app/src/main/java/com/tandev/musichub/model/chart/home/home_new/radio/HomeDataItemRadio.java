package com.tandev.musichub.model.chart.home.home_new.radio;

import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;
import com.tandev.musichub.model.chart.home.home_new.option.HomeOption;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDataItemRadio implements HomeDataItem, Serializable {
    private static final long serialVersionUID = 1L;
    private String sectionType;
    private String viewType;
    private String link;
    private String title;
    private String sectionId;
    private ArrayList<HomeDataItemRadioItem> items;

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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public ArrayList<HomeDataItemRadioItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<HomeDataItemRadioItem> items) {
        this.items = items;
    }
}
