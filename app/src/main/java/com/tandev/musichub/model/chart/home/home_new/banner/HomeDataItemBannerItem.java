package com.tandev.musichub.model.chart.home.home_new.banner;

import com.google.gson.Gson;

import java.io.Serializable;

public class HomeDataItemBannerItem implements Serializable {
    private int type;
    private String link;
    private String banner;
    private String cover;
    private String target;
    private String title;
    private String description;
    private int ispr;
    private String encodeId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIspr() {
        return ispr;
    }

    public void setIspr(int ispr) {
        this.ispr = ispr;
    }

    public String getEncodeId() {
        return encodeId;
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
    }
    public static HomeDataItemBannerItem fromJson(String json) {
        return new Gson().fromJson(json, HomeDataItemBannerItem.class);
    }

}
