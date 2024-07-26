package com.tandev.musichub.model.genre;

import java.io.Serializable;

public class GenreInfo implements Serializable {
    private int err;
    private String msg;
    private DataGenreInfo data;
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

    public DataGenreInfo getData() {
        return data;
    }

    public void setData(DataGenreInfo data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
