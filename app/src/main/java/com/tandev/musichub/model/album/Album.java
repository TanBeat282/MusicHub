package com.tandev.musichub.model.album;

import java.io.Serializable;

public class Album implements Serializable {
    private int err;
    private String msg;
    private DataAlbum data;
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

    public DataAlbum getData() {
        return data;
    }

    public void setData(DataAlbum data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
