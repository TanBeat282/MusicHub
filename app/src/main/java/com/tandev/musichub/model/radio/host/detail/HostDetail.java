package com.tandev.musichub.model.radio.host.detail;

import com.tandev.musichub.model.radio.host.info.DataHostInfo;

import java.io.Serializable;

public class HostDetail implements Serializable {
    private int err;
    private String msg;
    private DataHostDetail data;
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

    public DataHostDetail getData() {
        return data;
    }

    public void setData(DataHostDetail data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
