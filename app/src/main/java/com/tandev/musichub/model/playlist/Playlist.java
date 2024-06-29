package com.tandev.musichub.model.playlist;

import java.io.Serializable;

public class Playlist implements Serializable {
    private int err;
    private String msg;
    private DataPlaylist data;
    private long timestamp;

    public DataPlaylist getData() {
        return data;
    }

    public void setData(DataPlaylist data) {
        this.data = data;
    }

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
