package com.tandev.musichub.model.chart.home;

import java.io.Serializable;
import java.util.ArrayList;

public class DataHomePlaylist implements Serializable {
    private String sectionType;
    private String viewType;
    private String title;
    private ArrayList<ItemsDataHomePlaylist> items;

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

    public ArrayList<ItemsDataHomePlaylist> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsDataHomePlaylist> items) {
        this.items = items;
    }
}
