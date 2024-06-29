package com.tandev.musichub.model.search.search_suggestion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchSuggestions implements Serializable {
    private int err;
    private String msg;
    private long sTime;
    private SearchSuggestionsData data;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SearchSuggestionsData getData() {
        return data;
    }

    public void setData(SearchSuggestionsData data) {
        this.data = data;
    }

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }
}
