package com.tandev.musichub.model.video;

import java.io.Serializable;

public class Video implements Serializable {
    private int err;
    private String msg;
    private DataVideo data;
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

    public DataVideo getData() {
        return data;
    }

    public void setData(DataVideo data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
