package com.tandev.musichub.model.artist;


import java.io.Serializable;

public class ArtistDetail implements Serializable {
    private int err;
    private String msg;
    private DataArtist data;
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

    public DataArtist getData() {
        return data;
    }

    public void setData(DataArtist data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
