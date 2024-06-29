package com.tandev.musichub.model.artist.video;

import com.tandev.musichub.model.artist.SectionArtist;
import com.tandev.musichub.model.hub.HubVideo;
import com.tandev.musichub.model.hub.SectionHubVideo;
import com.tandev.musichub.model.playlist.DataPlaylist;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionArtistVideo implements SectionArtist, Serializable {
    private static final long serialVersionUID = 1L;
    private String sectionType;
    private String viewType;
    private String title;
    private String link;
    private String sectionId;
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
