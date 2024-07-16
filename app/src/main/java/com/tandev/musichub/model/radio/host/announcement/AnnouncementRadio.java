package com.tandev.musichub.model.radio.host.announcement;

import java.io.Serializable;

public class AnnouncementRadio implements Serializable {
    private int err;
    private String msg;
    private DataAnnouncementRadio data;
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

    public DataAnnouncementRadio getData() {
        return data;
    }

    public void setData(DataAnnouncementRadio data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
