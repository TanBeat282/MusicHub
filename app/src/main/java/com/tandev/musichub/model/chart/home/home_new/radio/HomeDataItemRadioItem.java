package com.tandev.musichub.model.chart.home.home_new.radio;

import java.io.Serializable;

public class HomeDataItemRadioItem implements Serializable {
    private int id;
    private String encodeId;
    private String title;
    private String thumbnail;
    private String thumbnailM;
    private String thumbnailV;
    private String thumbnailH;
    private String description;
    private int status;
    private String type;
    private String link;
    private String streaming;
    private RadioHost host;
    private int activeUsers;
    private RadioProgram program;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getThumbnailM() {
        return thumbnailM;
    }

    public void setThumbnailM(String thumbnailM) {
        this.thumbnailM = thumbnailM;
    }

    public String getThumbnailV() {
        return thumbnailV;
    }

    public void setThumbnailV(String thumbnailV) {
        this.thumbnailV = thumbnailV;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getStreaming() {
        return streaming;
    }

    public void setStreaming(String streaming) {
        this.streaming = streaming;
    }

    public RadioHost getHost() {
        return host;
    }

    public void setHost(RadioHost host) {
        this.host = host;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public RadioProgram getProgram() {
        return program;
    }

    public void setProgram(RadioProgram program) {
        this.program = program;
    }
}
