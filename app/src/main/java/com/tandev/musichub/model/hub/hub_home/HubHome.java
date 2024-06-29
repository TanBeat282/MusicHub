package com.tandev.musichub.model.hub.hub_home;


import java.io.Serializable;

public class HubHome implements Serializable {
    private int err;
    private String msg;
    private DataHubHome data;
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

    public DataHubHome getData() {
        return data;
    }

    public void setData(DataHubHome data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
