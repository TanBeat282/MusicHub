package com.tandev.musichub.model.video.section_relate;

import com.tandev.musichub.model.hub.HubVideo;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSectionRelateVideo implements Serializable {
    public String sectionType;
    public String viewType;
    public String title;
    public String link;
    public String sectionId;
    private ArrayList<HubVideo> items;

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

    public ArrayList<HubVideo> getItems() {
        return items;
    }

    public void setItems(ArrayList<HubVideo> items) {
        this.items = items;
    }
}
