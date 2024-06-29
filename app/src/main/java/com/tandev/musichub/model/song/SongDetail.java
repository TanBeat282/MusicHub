package com.tandev.musichub.model.song;

import java.io.Serializable;

public class SongDetail implements Serializable {
    private int err;
    private String msg;
    private DataSongDetail data;
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

    public DataSongDetail getData() {
        return data;
    }

    public void setData(DataSongDetail data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
