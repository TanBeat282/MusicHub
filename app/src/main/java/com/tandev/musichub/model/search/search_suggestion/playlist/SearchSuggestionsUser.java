package com.tandev.musichub.model.search.search_suggestion.playlist;

import java.io.Serializable;

public class SearchSuggestionsUser implements Serializable {
    private int id;
    private String euId;
    private String name;
    private String avatar;
    private long createdTime;
    private int boolAtt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEuId() {
        return euId;
    }

    public void setEuId(String euId) {
        this.euId = euId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getBoolAtt() {
        return boolAtt;
    }

    public void setBoolAtt(int boolAtt) {
        this.boolAtt = boolAtt;
    }
}
