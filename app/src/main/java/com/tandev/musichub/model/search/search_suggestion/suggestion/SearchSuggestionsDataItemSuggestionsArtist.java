package com.tandev.musichub.model.search.search_suggestion.suggestion;

import java.io.Serializable;

public class SearchSuggestionsDataItemSuggestionsArtist implements SearchSuggestionsDataItemSuggestionsItem, Serializable {
    private int type;
    private String id;
    private int oaType;
    private int artistType;
    private String name;
    private String aliasName;
    private String avatar;
    private String link;
    private int followers;
    private String tracking;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOaType() {
        return oaType;
    }

    public void setOaType(int oaType) {
        this.oaType = oaType;
    }

    public int getArtistType() {
        return artistType;
    }

    public void setArtistType(int artistType) {
        this.artistType = artistType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
