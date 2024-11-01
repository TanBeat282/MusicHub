package com.tandev.musichub.model.chart.home.home_new.song_station;

import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;

import java.io.Serializable;

public class HomeDataItemSongStation implements HomeDataItem, Serializable  {
    private String sectionId;
    private String sectionType;
    private String title;
    private String subtitle;
    private int songsPerCol;
    private boolean isPlayAll;
    private boolean isRefresh;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getSongsPerCol() {
        return songsPerCol;
    }

    public void setSongsPerCol(int songsPerCol) {
        this.songsPerCol = songsPerCol;
    }

    public boolean isPlayAll() {
        return isPlayAll;
    }

    public void setPlayAll(boolean playAll) {
        isPlayAll = playAll;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
