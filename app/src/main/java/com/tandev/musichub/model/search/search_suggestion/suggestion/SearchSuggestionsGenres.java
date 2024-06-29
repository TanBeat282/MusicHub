package com.tandev.musichub.model.search.search_suggestion.suggestion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchSuggestionsGenres implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("thumbS")
    private String thumbS;

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

    public String getThumbS() {
        return thumbS;
    }

    public void setThumbS(String thumbS) {
        this.thumbS = thumbS;
    }
}
