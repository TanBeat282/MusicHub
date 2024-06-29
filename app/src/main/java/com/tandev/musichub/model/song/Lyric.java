package com.tandev.musichub.model.song;

import java.io.Serializable;

public class Lyric implements Serializable {
    private int err;
    private String msg;
    private DataLyric data;
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

    public DataLyric getData() {
        return data;
    }

    public void setData(DataLyric data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
