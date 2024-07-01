package com.tandev.musichub.model.search.search_multil.search_top;

import java.io.Serializable;

public class SearchMultiDataTopArtist implements Serializable {
    private String id;
    private String name;
    private String link;
    private boolean spotlight;
    private String alias;
    private String playlistId;
    private String cover;
    private String thumbnail;
    private String thumbnailM;
    private String objectType;
    private boolean isOA;
    private boolean isOABrand;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isSpotlight() {
        return spotlight;
    }

    public void setSpotlight(boolean spotlight) {
        this.spotlight = spotlight;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
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

    public String getThumbnailM() {
        return thumbnailM;
    }

    public void setThumbnailM(String thumbnailM) {
        this.thumbnailM = thumbnailM;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public boolean isOA() {
        return isOA;
    }

    public void setOA(boolean OA) {
        isOA = OA;
    }

    public boolean isOABrand() {
        return isOABrand;
    }

    public void setOABrand(boolean OABrand) {
        isOABrand = OABrand;
    }
}
