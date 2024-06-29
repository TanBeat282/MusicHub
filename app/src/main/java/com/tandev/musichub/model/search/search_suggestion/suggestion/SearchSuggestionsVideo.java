package com.tandev.musichub.model.search.search_suggestion.suggestion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchSuggestionsVideo implements Serializable {
    @SerializedName("playStatus")
    private int playStatus;
    @SerializedName("status")
    private int status;

    public int getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(int playStatus) {
        this.playStatus = playStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
