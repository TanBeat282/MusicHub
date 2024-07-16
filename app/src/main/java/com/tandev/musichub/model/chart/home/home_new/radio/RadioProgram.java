package com.tandev.musichub.model.chart.home.home_new.radio;

import java.io.Serializable;
import java.util.List;

public class RadioProgram implements Serializable {
    private String encodeId;
    private String title;
    private String thumbnail;
    private String thumbnailH;
    private String description;
    private long startTime;
    private long endTime;
    private boolean hasSongRequest;
    private List<String> genreIds;

    public String getEncodeId() {
        return encodeId;
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailH() {
        return thumbnailH;
    }

    public void setThumbnailH(String thumbnailH) {
        this.thumbnailH = thumbnailH;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isHasSongRequest() {
        return hasSongRequest;
    }

    public void setHasSongRequest(boolean hasSongRequest) {
        this.hasSongRequest = hasSongRequest;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }
}
