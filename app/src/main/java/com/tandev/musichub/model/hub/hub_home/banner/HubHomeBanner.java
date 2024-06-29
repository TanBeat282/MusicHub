package com.tandev.musichub.model.hub.hub_home.banner;

import java.io.Serializable;

public class HubHomeBanner implements Serializable {
    private String cover;
    private String link;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
