package com.tandev.musichub.model.preview_premium;

import java.io.Serializable;

public class BumperAudio implements Serializable {
    private String link;
    private int position;
    private int type;
    private int id;
    private int displayType;
    private String campaignKey;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getCampaignKey() {
        return campaignKey;
    }

    public void setCampaignKey(String campaignKey) {
        this.campaignKey = campaignKey;
    }
}
