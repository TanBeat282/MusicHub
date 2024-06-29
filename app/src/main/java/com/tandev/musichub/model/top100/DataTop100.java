package com.tandev.musichub.model.top100;

import com.tandev.musichub.model.playlist.DataPlaylist;

import java.io.Serializable;
import java.util.ArrayList;

public class DataTop100 implements Serializable {
    private String sectionType;
    private String viewType;
    private String title;
    private String link;
    private String sectionId;
    private ArrayList<DataPlaylist> items;
    private GenreTop100 genre;

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

    public ArrayList<DataPlaylist> getItems() {
        return items;
    }

    public void setItems(ArrayList<DataPlaylist> items) {
        this.items = items;
    }

    public GenreTop100 getGenre() {
        return genre;
    }

    public void setGenre(GenreTop100 genre) {
        this.genre = genre;
    }
}
