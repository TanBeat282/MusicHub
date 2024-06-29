package com.tandev.musichub.model.search.song;

import com.tandev.musichub.model.search.search_multil.SearchMultiData;

import java.io.Serializable;

public class SearchSong implements Serializable {
    private int err;
    private String msg;
    private SearchSongData data;
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

    public SearchSongData getData() {
        return data;
    }

    public void setData(SearchSongData data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
