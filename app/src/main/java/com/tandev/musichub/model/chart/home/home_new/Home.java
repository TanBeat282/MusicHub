package com.tandev.musichub.model.chart.home.home_new;

import com.tandev.musichub.model.chart.home.home_new.data.HomeData;

import java.io.Serializable;

public class Home implements Serializable {
    private int err;
    private String msg;
    private HomeData data;
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

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
