package com.tandev.musichub.model.hub;

import java.io.Serializable;
import java.util.ArrayList;

public class DataHub implements Serializable {
    private String encodeId;
    private String cover;
    private String thumbnail;
    private String thumbnailHasText;
    private String thumbnailR;
    private String title;
    private String link;
    private String description;
    private ArrayList<HubSection> sections;

    public String getEncodeId() {
        return encodeId;
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailHasText() {
        return thumbnailHasText;
    }

    public void setThumbnailHasText(String thumbnailHasText) {
        this.thumbnailHasText = thumbnailHasText;
    }

    public String getThumbnailR() {
        return thumbnailR;
    }

    public void setThumbnailR(String thumbnailR) {
        this.thumbnailR = thumbnailR;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<HubSection>  getSections() {
        return sections;
    }

    public void setSections(ArrayList<HubSection>  sections) {
        this.sections = sections;
    }
}
