package com.tandev.musichub.model.radio.host.info;

import java.io.Serializable;

public class HostInfo implements Serializable {
    private int err;
    private String msg;
    private DataHostInfo data;
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

    public DataHostInfo getData() {
        return data;
    }

    public void setData(DataHostInfo data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
