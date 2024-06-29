package com.tandev.musichub.model.search.search_multil;

import com.tandev.musichub.model.search.search_suggestion.SearchSuggestionsData;
import com.tandev.musichub.model.song.DataSongAudio;

import java.io.Serializable;

public class SearchMulti implements Serializable {
    private int err;
    private String msg;
    private SearchMultiData data;
    private long timestamp;

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

    public SearchMultiData getData() {
        return data;
    }

    public void setData(SearchMultiData data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
